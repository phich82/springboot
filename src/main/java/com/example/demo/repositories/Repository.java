package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.transaction.Transactional;

import org.hibernate.internal.SessionImpl;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.exceptions.CommonException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.services.Helper;


public abstract class Repository {
    @Autowired
    private EntityManager entityManager;

    private String table = null;
    protected String pkey = "id";
    private String selectedColumns = "*";

    protected abstract Class<?> model();

    public List<String> getColumns() {
        EntityType<?> entity = this.entityManager.getMetamodel().entity(this.model());
        List<String> columns = new ArrayList<String>();
        for (Attribute<?,?> attr: entity.getAttributes()) {
            System.out.println("column => " + attr.getName());
            columns.add(attr.getName());
        }
        return columns;
    }

    public Map<String, Object> getColumnsWithType() {
        EntityType<?> entity = this.entityManager.getMetamodel().entity(this.model());
        Map<String, Object> columns = new HashMap<String, Object>();
        for (Attribute<?,?> attr: entity.getAttributes()) {
            columns.put(attr.getName(), attr.getJavaType().getSimpleName());
        }
        return columns;
    }

    public String getPrimarykey() {
        Class<?> entityClass = this.model();
        SessionImpl session = this.entityManager.unwrap(SessionImpl.class);
        EntityPersister persister = session.getEntityPersister(entityClass.getName(), entityClass);
        if (persister instanceof AbstractEntityPersister) {
            AbstractEntityPersister persisterImpl = (AbstractEntityPersister) persister;
            // return persister.getIdentifierPropertyName();
            return Helper.implode(persisterImpl.getIdentifierColumnNames(), ",");
        }
        throw new CommonException("Unexpected persister type. A subtype of AbstractEntityPersister expected.");
    }

    public String getTable() {
        if (this.table != null) {
            return this.table;
        }
        Class<?> entityClass = this.model();
        Table t = entityClass.getAnnotation(Table.class);
        this.table = (t == null) ? this.entityManager.getMetamodel().entity(entityClass).getName().toLowerCase() + "s" : t.name();
        return this.table;

        // SessionImpl session = this.entityManager.unwrap(SessionImpl.class);
        // EntityPersister persister = session.getEntityPersister(entityClass.getName(), entityClass);

        // if (persister instanceof AbstractEntityPersister) {
        //     AbstractEntityPersister persisterImpl = (AbstractEntityPersister) persister;
        //     String tableName = persisterImpl.getTableName();
        //     this.table = (tableName != null) ? tableName : persisterImpl.getRootTableName();
        //     return this.table;
        // }
        // throw new CommonException("Unexpected persister type; a subtype of AbstractEntityPersister expected.");
    }

    public void beginTransaction() {
        if (!this.entityManager.getTransaction().isActive()) {
            this.entityManager.getTransaction().begin();
        }
    }

    public void rollback() {
        this.entityManager.getTransaction().rollback();
    }

    public void commit() {
        this.entityManager.getTransaction().commit();
    }

    public Repository select(String columns) {
        this.selectedColumns = columns;
        return this;
    }

    public Repository select(String[] columns) {
        this.selectedColumns = Helper.implode(columns, ",");
        return this;
    }

    public Repository select(List<String> columns) {
        this.selectedColumns = Helper.implode(columns, ",");
        return this;
    }

    public String primaryKey() {
        return this.getPrimarykey();
    }

    public Query query(String sql) {
        System.out.println("[_query][model] => " + this.model());
        return this._query(sql);
    }

    public Query query(String sql, List<?> binding) {
        System.out.println("[_query][model] => " + this.model());
        System.out.println("[query][binding][list] => " + binding);
        Query q = this._query(sql);
        for (int i=0; i < binding.size(); i++) {
            q.setParameter(i + 1, binding.get(i));
        }
        return q;
    }

    public Query query(String sql, Map<String, ?> binding) {
        System.out.println("[_query][model] => " + this.model());
        System.out.println("[query][binding][map] => " + binding);
        Query q = this._query(sql);
        binding.forEach(q::setParameter);
        return q;
    }

    public Object findOne(String sql) {
        try {
            return this.query(sql).getSingleResult();
        } catch (Exception e) {
            // Logs
            System.out.println("[findOne][error] => " + e);

            return null;
        }
    }

    public Object findOne(String sql, List<?> binding) {
        try {
            return this.query(sql, binding).getSingleResult();
        } catch (Exception e) {
            // Logs
            System.out.println("[findOne][error] => " + e);

            return null;
        }
    }

    public Object findOne(String sql, Map<String, ?> binding) {
        try {
            return this.query(sql, binding).getSingleResult();
        } catch (Exception e) {
            // Logs
            System.out.println("[findOne][error] => " + e);

            return null;
        }
    }

    public Object findOneOrFailed(int id) {
        String sql = String.format("SELECT %s FROM %s WHERE %s=?", this.selectedColumns, this.getTable(), this.primaryKey());
        Object result = this.findOne(sql, Arrays.asList(id));
        this._reset();
        if (result == null) {
            throw new NotFoundException();
        }
        return result;
    }

    public List<?> find() {
        return this.findBy(null);
    }

    public List<?> find(String sql) {
        try {
            return this.query(sql).getResultList();
        } catch (Exception e) {
            // Logs
            System.out.println("[find][error] => " + e);

            return null;
        }
    }

    public List<?> find(String sql, List<?> binding) {
        try {
            return this.query(sql, binding).getResultList();
        } catch (Exception e) {
            // Logs
            System.out.println("[find][list][error] => " + e);

            return null;
        }
    }

    public List<?> find(String sql, Map<String, ?> binding) {
        return this.query(sql, binding).getResultList();
    }

    public Object findOrFailed(int id) {
        return this.findOneOrFailed(id);
    }

    public Object find(int id, boolean... useSql) {
        try {
            // No use sql
            if (this._isUseSql(useSql)) {
                System.out.println("[find] => use sql");
                return this.findOneOrFailed(id);
            }
            System.out.println("[find] => no use sql");
            return this.entityManager.find(this.model(), id);
        } catch (Exception e) {
            // Logs
            System.out.println("[find][error] => " + e);

            return null;
        }
    }

    public Object findBy(int id, boolean... useSql) {
        return this.find(id, useSql);
    }

    public List<?> findBy(Map<String, ?> conditions) {
        System.out.println("[findBy][input] => " + conditions);

        String sql = String.format("SELECT %s FROM %s", this.selectedColumns, this.getTable());

        if (conditions == null || conditions.size() < 1) {
            List<?> result = this.find(sql);
            this._reset();
            return result;
        }
        Object[] clause = this._buildWhereClause(conditions);
        List<?> binding = (List<?>) clause[1];

        System.out.println("[findBy][clause] => " + clause);
        System.out.println("[findBy][binding] => " + binding);
        sql += clause[0];

        List<?> result = this.find(sql, binding);
        this._reset();
        return result;
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> params) {
        Object[] paramsInsert = this._buildInsertParams(params);
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING %s", this.getTable(), paramsInsert[0], paramsInsert[1], this.primaryKey());
        params.put(this.getPrimarykey(), this.query(sql).executeUpdate());
        return params;
    }

    public boolean save(List<?> entities) {
        this.beginTransaction();
        try {
            // for (int i=0; i < entities.size(); i++) {
            //     if (entities.get(i) instanceof List) {
            //         this.save(entities.get(i));
            //     } else if (entities.get(i).getClass() == this.model().getClass()) {
            //         this.save(entities.get(i));
            //     } else {
            //         throw new CommonException("Input only can be an array of entities or of an array.");
            //     }
            // }
            for (Object entity: entities) {
                this.save(entity);
            }
            this.commit();
            return true;
        } catch (Exception e) {
            this.rollback();
            // Logs
            System.out.println("[save][error] => " + e);

            return false;
        }
    }

    @Transactional
    public Object save(Object entity) {
        this.entityManager.persist(entity);
        return entity;
    }

    @Transactional
    public boolean updateBy(Map<String, ?> conditions, Map<String, ?> params) {
        List<?> records = this.findBy(params);
        if (records == null || records.size() < 1) {
            throw new NotFoundException();
        }

        List<Object> paramsSet = new ArrayList<Object>();
        List<Object> bindingSet = new ArrayList<Object>();

        for (Map.Entry<String, ?> row: params.entrySet()) {
            paramsSet.add(row.getKey() + "=" + "?");
            bindingSet.add(row.getValue());
        }

        Object[] clause = this._buildWhereClause(conditions);
        List<?> _bindingWhere = (List<?>) clause[1];

        List<Object> binding = new ArrayList<Object>(bindingSet);
        binding.addAll(_bindingWhere);

        String whereClause = (String) clause[0];
        String sqlSet = Helper.implode(paramsSet, ", ");
        String sql = String.format("UPDATE %s SET %s %s", this.getTable(), sqlSet, whereClause);

        return this.query(sql, binding).executeUpdate() > 0;
    }

    @Transactional
    public boolean update(int id, Map<String, ?> params) {
        Object record = this.find(id);
        if (record == null) {
            throw new NotFoundException();
        }

        List<Object> _params = new ArrayList<Object>();
        List<Object> _binding = new ArrayList<Object>();

        for (Map.Entry<String, ?> row: params.entrySet()) {
            _params.add(row.getKey() + "=" + "?");
            _binding.add(row.getValue());
        }

        _binding.add(id);

        String setSql = Helper.implode(_params, ", ");
        String sql = String.format("UPDATE %s SET %s WHERE %s=?", this.getTable(), setSql, this.primaryKey());

        return this.query(sql, _binding).executeUpdate() > 0;
    }

    @Transactional
    public boolean update(Object entity) {
        return this.entityManager.merge(entity) != null;
    }

    @Transactional
    public boolean update(String sql) {
        return this.query(sql).executeUpdate() > 0;
    }

    @Transactional
    public boolean update(String sql, List<?> binding) {
        return this.query(sql, binding).executeUpdate() > 0;
    }

    @Transactional
    public boolean update(String sql, Map<String, ?> binding) {
        return this.query(sql, binding).executeUpdate() > 0;
    }

    @Transactional
    public boolean destroy(int id, boolean... useSql) {
        Object record = this.find(id, useSql);
        if (record == null) {
            throw new NotFoundException();
        }
        System.out.println("[destroy][record] => " + record);
        // no use sql
        if (!this._isUseSql(useSql)) {
            this.entityManager.remove(record);
            return true;
        }
        String sql = String.format("DELETE FROM %s WHERE %s=%d", this.getTable(), this.primaryKey(), id);
        return this.query(sql).executeUpdate() > 0;
    }

    @Transactional
    public boolean destroy(int[] ids) {
        String sql = String.format("DELETE FROM %s WHERE %s IN (%s)", this.getTable(), this.primaryKey(), Helper.implode(Arrays.asList(ids), ","));
        return this.query(sql).executeUpdate() > 0;
    }

    @Transactional
    public boolean destroyBy(Map<String, ?> conditions) throws Exception {
        if (conditions.size() < 1) {
            throw new Exception("[destroyBy] Input parameters is empty.");
        }

        List<?> records = this.findBy(conditions);
        if (records == null || records.size() < 1) {
            throw new NotFoundException();
        }

        String sql = String.format("DELETE FROM %s", this.getTable());
        // Build where clause
        Object[] clause = this._buildWhereClause(conditions);
        List<?> binding = (List<?>) clause[1];
        sql += clause[0];

        return this.query(sql, binding).executeUpdate() > 0;
    }

    @Transactional
    public boolean destroyBy(String sql, List<?> binding) {
        return this.query(sql, binding).executeUpdate() > 0;
    }

    @Transactional
    public boolean destroyBy(String sql, Map<String, ?> binding) {
        return this.query(sql, binding).executeUpdate() > 0;
    }

    public Object[] buildWhere(Map<String, ?> conditions, boolean... withPrefixWhere) {
        return this._buildWhereClause(conditions, withPrefixWhere);
    }

    private Object[] _buildWhereClause(Map<String, ?> conditions, boolean... withPrefixWhere) {
        String where = "";
        String operator = "=";
        String placeholder = "?";

        List<Object> params = new ArrayList<Object>();
        List<Object> binding = new ArrayList<Object>();

        if (conditions.size() < 1) {
            Object[] result = {where, binding};
            return result;
        }

        where = (withPrefixWhere.length == 0 ||  withPrefixWhere[0] == true) ? " WHERE " : "";

        // Build where clause
        for (Map.Entry<String, ?> row: conditions.entrySet()) {
            String column = row.getKey();
            Object value = row.getValue();
            if (row.getValue() instanceof List) {
                List<?> v = (List<?>) row.getValue();
                if (v.size() == 1) {
                    value = v.get(0);
                } else if (v.size() > 1) {
                    operator = (String) v.get(0);
                    value = v.get(1);
                }
            } else if (row.getValue() instanceof Object[]) {
                Object[] v = (Object[]) row.getValue();
                if (v.length == 1) {
                    value = v[0];
                } else if (v.length > 1) {
                    operator = (String) v[0];
                    value = v[1];
                }
            }
            params.add(column + operator + placeholder);
            binding.add(value);
        }

        if (params.size() > 0 && params.size() == binding.size()) {
            where += Helper.implode(params, " AND ");
            Object[] result = {where, binding};
            return result;
        }

        throw new CommonException("[_buildWhereClause] => `conditions` parameter invalid.");
    }

    private Object[] _buildInsertParams(Map<String, ?> params) {
        String valuesInsert = "";
        String columnsInsert = "";
        Map<String, Object> columnsWithType = this.getColumnsWithType();
        Object[] columns = columnsWithType.keySet().toArray();
        for (int i=0; i < columns.length; i++) {
            String column = (String) columns[i];
            // Ignore primary key
            if (column.equals(this.getPrimarykey())) {
                continue;
            }
            String columnType = (String) columnsWithType.get(column);
            Object value = params.containsKey(column) ? params.get(column) : null;
            if (i == 0) {
                columnsInsert += column;
                valuesInsert += columnType.equals("String") ? "'" + value + "'" : value;
            } else {
                columnsInsert += "," + column;
                valuesInsert += "," + (columnType .equals("String") ? "'" + value + "'" : value);
            }
        }

        Object[] result = {columnsInsert, valuesInsert};

        return result;
    }

    private Query _query(String sql) {
        System.out.println("[_query][sql] => " + sql);
        return this.entityManager.createNativeQuery(sql, this.model());
    }

    private void _reset() {
        this.selectedColumns = "*";
        this.pkey = "id";
    }

    private void _close() {
        if (this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    private boolean _isUseSql(boolean[] useSql) {
        return useSql.length > 0 && useSql[0] == true;
    }

}
