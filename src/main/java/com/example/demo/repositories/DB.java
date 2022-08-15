package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Tuple;
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
import com.example.demo.services.Logger;


public class DB {

    @Autowired
    private EntityManager entityManager;

    private String _table = null;
    private String _pkey = "id";
    private String _selectedColumns = "*";
    private boolean _holderIndex = true;
    private boolean _prefixWhere = true;
    private Class<?> _entity = null;
    private int _batch = 100;


    protected Class<?> model() {
        return null;
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

    public String getTable() {
        if (this._table != null) {
            return this._table;
        }
        if (this._entity == null) {
            this._entity = this.model();
        }
        if (this._entity == null) {
            this._table = null;
            return "";
        }
        Table t = this._entity.getAnnotation(Table.class);
        this._table = (t == null) ? this.entityManager.getMetamodel().entity(this._entity).getName().toLowerCase() + "s" : t.name();
        return this._table;
    }

    public List<String> getColumns() {
        EntityType<?> entity = this.entityManager.getMetamodel().entity(this.model());
        List<String> columns = new ArrayList<String>();
        for (Attribute<?,?> attr: entity.getAttributes()) {
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

    public Query query(String sql, Object... binding) {
        if (binding.length > 0) {
            System.out.println("[DB][query][binding] => " + binding[0]);
        }
        System.out.println("[DB][query][sql] => " + sql);

        Query q = this.entityManager.createNativeQuery(sql, Tuple.class);
        // Binding data into sql
        if (binding.length > 0) {
            if (binding[0] instanceof List) {
                List<?> b = (List<?>) binding[0];
                for (int i=0; i < b.size(); i++) {
                    q.setParameter(i + 1, b.get(i));
                }
            } else if (binding[0] instanceof Map) {
                Map<String, Object> _binding = (Map<String, Object>) binding[0];
                _binding.forEach(q::setParameter);
            }
        }
        return q;
    }

    public DB select(String selectedColumns) {
        this._selectedColumns = selectedColumns;
        return this;
    }

    public DB table(String tableName) {
        this._table = tableName;
        return this;
    }

    public DB setBatch(int batch) {
        this._batch = batch;
        return this;
    }

    public DB withHolderIndex() {
        this._holderIndex = true;
        return this;
    }

    public DB withHolderColumn() {
        this._holderIndex = false;
        return this;
    }

    public DB withPrefixWhere() {
        this._prefixWhere = true;
        return this;
    }

    public DB notPrefixWhere() {
        this._prefixWhere = false;
        return this;
    }

    private void _reset() {
        this._pkey = "id";
        this._selectedColumns = "*";
        this._table = null;
        this._holderIndex = true;
        this._entity = this.model();
        this._prefixWhere = true;
        this._batch = 100;
    }

    private void _resolvePrimaryKey(Object[] pk) {
        if (pk.length > 0 && pk[0] != null && !pk[0].equals("")) {
            this._pkey = (String) pk[0];
        }
    }

    private String _getHolder(String... column) {
        if (this._holderIndex) {
            return "?";
        }
        return String.format(":%s", column[0]);
    }

    private String _getPrefixWhere() {
        return this._prefixWhere ? " WHERE " : "";
    }

    private void _validateParams(Object[] params) {
        if (params == null || params.length < 1) {
            this._reset();
            Logger.error("[DB][array][_validateParams] Parameters are empty.", this);
            throw new CommonException("[DB][array][_validateParams] Parameters are empty.");
        }
    }

    private void _validateParams(List<?> params) {
        if (params == null || params.size() < 1) {
            this._reset();
            Logger.error("[DB][list][_validateParams] Parameters are empty.", this);
            throw new CommonException("[DB][list][_validateParams] Parameters are empty.");
        }
    }

    private void _validateParams(Map<String, ?> params) {
        if (params == null || params.size() < 1) {
            Logger.error("[DB][map][_validateParams] Parameters are empty.", this);
            this._reset();
            throw new CommonException("[DB][map][_validateParams] Parameters are empty.");
        }
    }

    private void _validateTable() {
        String tableName = this.getTable();
        if (tableName == null || tableName.equals("")) {
            Logger.error("Missing table.", this);
            this._reset();
            throw new NotFoundException("Missing table.");
        }
    }

    private void _validateEntity() {
        if (this._entity == null || !(this._entity instanceof Class<?>)) {
            Logger.error("Missing entity.", this);
            this._reset();
            throw new NotFoundException("Missing entity.");
        }
    }

    private void close() {
        this.entityManager.close();
    }

    private int executeUpdate(String sql, Object... binding) {
        int result = this.query(sql, binding).executeUpdate();
        this.close();
        return result;
    }

    public DB primaryKey(String pkey) {
        this._pkey = pkey;
        return this;
    }

    public DB pk(String pkey) {
        return this.primaryKey(pkey);
    }

    public DB entity(Class<?> _entity) {
        this._entity = _entity;
        return this;
    }

    public List<Map<String, ?>> raw(String sql, Object... binding) {
        List<Tuple> fetchedRows = new ArrayList<Tuple>();
        try {
            fetchedRows = this.query(sql, binding).getResultList();
        } catch (Exception e) {
            this._reset();
            throw new CommonException(e.toString());
        }

        // Format fetched rows
        List<Map<String, ?>> recordList = new ArrayList<>();
        fetchedRows.forEach(row -> {
            Map<String, Object> formattedRow = new HashMap<>();
            row.getElements().forEach(column -> {
                String columnName = column.getAlias();
                Object columnValue = row.get(column);
                formattedRow.put(columnName, columnValue);
            });
            recordList.add(formattedRow);
        });

        this._reset();
        this.close();

        return recordList;
    }

    public Map<String, ?> find(Object id, String... pk) {
        this._validateTable();
        this._resolvePrimaryKey(pk);
        String sql = String.format("SELECT %s FROM %s WHERE %s=%s", this._selectedColumns, this.getTable(), this._pkey, this._getHolder(this._pkey));

        List<Map<String, ?>> result = this.raw(sql, Arrays.asList(id));
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public Map<String, ?> findOrFailed(Object id, String... pk) {
        Map<String, ?> result = this.find(id, pk);
        if (result == null) {
            throw new NotFoundException();
        }
        return result;
    }

    public List<Map<String, ?>> findAll() {
        return this.findBy(null);
    }

    public List<Map<String, ?>> findBy(Map<String, ?> params) {
        this._validateTable();
        // Get all
        if (params == null) {
            return this.raw(String.format("SELECT %s FROM %s", this._selectedColumns, this.getTable()));
        }
        // Get by conditions
        Object[] whereClause = this.buildSql(params);
        String whereSql = (String) whereClause[0];
        Object binding = whereClause[1];

        String sql = String.format("SELECT %s FROM %s %s", this._selectedColumns, this.getTable(), whereSql);
        return this.raw(sql, binding);
    }

    public boolean exists(Object id, String... pk) {
        return this.find(id, pk) != null;
    }

    public boolean existsBy(Map<String, Object> params) {
        return this.findBy(params).size() > 0;
    }

    @Transactional
    public Object save(Map<String, Object> params) {
        this._validateTable();
        this._validateParams(params);

        Object result = null;

        Object[] paramsInsert = this.buildInsertSql(params);
        String colunmsInsert = (String) paramsInsert[0];
        String holdersInsert = (String) paramsInsert[1];
        List<?> bindingInsert = (List<?>) paramsInsert[2];

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING %s", this.getTable(), colunmsInsert, holdersInsert, this._pkey);

        Object lastId = this.executeUpdate(sql, bindingInsert);

        if (lastId == null || (int) lastId < 1) {
            Logger.error("Could not update.", this);
            throw new CommonException("Could not update.");
        } else {
            // Add last id after inserted to response
            params.put(this._pkey, (int) lastId);
            result = params;
        }

        return result;
    }

    @Transactional
    public Object save(List<Map<String, Object>> params) {
        int count = 0;
        for (Map<String, Object> param: params) {
            if (this.save(param) != null) {
                count++;
            }
        }
        return count;
    }

    @Transactional
    public Object saveBatch(List<Map<String, Object>> params) {
        int count = 0;

        return count;
    }

    @Transactional
    public Object save(Object objEntity) {
        this._validateEntity();
        this.entityManager.persist(objEntity);
        this._reset();
        return objEntity;
    }

    @Transactional
    public boolean update(Object objEntity) {
        this._validateEntity();
        boolean result = this.entityManager.merge(objEntity) != null;
        this._reset();
        return result;

    }

    @Transactional
    public boolean update(Object id, Map<String, Object> params) {
        this._validateTable();
        this._validateParams(params);

        Object[] paramsUpdate = this.buildUpdateSql(params);
        String sqlSet = (String) paramsUpdate[0];
        List<Object> binding = (List<Object>) paramsUpdate[1];
        // Add more id
        binding.add(id);

        String sql = String.format("UPDATE %s SET %s WHERE %s=%s", this.getTable(), sqlSet, this._pkey, this._getHolder(this._pkey));

        return this.executeUpdate(sql, binding)> 0;
    }

    @Transactional
    public int updateBy(Map<String, Object> conditions, Map<String, Object> params) {
        this._validateTable();
        this._validateParams(conditions);
        this._validateParams(params);

        // 1st: build params for SET clause
        Object[] paramsUpdateSet = this.buildUpdateSql(params);
        String sqlSet = (String) paramsUpdateSet[0];
        List<Object> bindingSet = (List<Object>) paramsUpdateSet[1];
        // 2nd: build params for WHERE clause
        Object[] paramsUpdateWhere = this.buildSql(params);
        String sqlWhere = (String) paramsUpdateWhere[0];
        List<Object> bindingWhere = (List<Object>) paramsUpdateWhere[1];
        // Merge binding
        List<Object> binding = new ArrayList<Object>(bindingSet);
        binding.addAll(bindingWhere);

        String sql = String.format("UPDATE %s SET % %s", this.getTable(), sqlSet, sqlWhere);

        return this.executeUpdate(sql, binding);
    }

    public int updateBatch(List<Map<String, Object>> bindings) {
        int count = 0;

        return count;
    }

    @Transactional
    public boolean delete(Object id, String... pk) {
        this._resolvePrimaryKey(pk);
        Map<String, ?> record = this.find(id, this._pkey);
        if (record == null) {
            Logger.error(String.format("ID [%s] not exists.", id), this);
            this._reset();
            throw new NotFoundException(String.format("ID [%s] not exists.", id));
        }
        // Delete via entity
        if (this._entity != null && id instanceof Class<?>) {
            this.entityManager.remove(id);
            this._reset();
            return true;
        }
        // Delete via sql
        Object[] ids = {id};
        return this.deleteMany(ids) > 0;
    }

    @Transactional
    public int deleteMany(Object[] ids, String... pk) {
        this._validateTable();
        this._resolvePrimaryKey(pk);
        this._validateParams(ids);

        String clauseIN = Helper.implode(ids, ",");
        String sql = String.format("DELETE FROM %s WHERE %s IN(%s)", this.getTable(), this._pkey, clauseIN);
        return this.executeUpdate(sql);
    }

    @Transactional
    public int deleteBy(Map<String, Object> conditions) {
        this._validateTable();
        this._validateParams(conditions);

        Object[] whereClause = this.buildSql(conditions);
        String whereSql = (String) whereClause[0];
        List<?> binding = (List<?>) whereClause[1];

        String sql = String.format("DELETE FROM %s %s", this.getTable(), whereSql);

        return this.query(sql, binding).executeUpdate();
    }

    public Object[] buildInsertSql(Map<String, Object> params) {
        List<String> columnsInsert = new ArrayList<String>();
        List<Object> holdersInsert = new ArrayList<Object>();
        List<Object> valuesInsert = new ArrayList<Object>();
        Map<String, Object> valuesInsertHash = new HashMap<String, Object>();

        for (Map.Entry<String, Object> row: params.entrySet()) {
            columnsInsert.add(row.getKey());
            holdersInsert.add(this._getHolder(row.getKey()));
            if (this._holderIndex) {
                valuesInsert.add(row.getValue());
            } else {
                valuesInsertHash.put(row.getKey(), row.getValue());
            }
        }

        Object[] result = {
            Helper.implode(columnsInsert, ","),
            Helper.implode(holdersInsert, ","),
            this._holderIndex ? valuesInsert : valuesInsertHash
        };

        return result;
    }

    public Object[] buildInsertBatchSql(List<Map<String, Object>> params) {
        return null;
    }

    public Object[] buildUpdateSql(Map<String, Object> params) {
        List<String> columnsUpdate = new ArrayList<String>();
        List<Object> valuesUpdate = new ArrayList<Object>();
        Map<String, Object> valuesUpdateHash = new HashMap<String, Object>();

        for (Map.Entry<String, Object> row: params.entrySet()) {
            columnsUpdate.add(row.getKey() + "=" + this._getHolder(row.getKey()));
            if (this._holderIndex) {
                valuesUpdate.add(row.getValue());
            } else {
                valuesUpdateHash.put(row.getKey(), row.getValue());
            }
        }

        Object[] result = {
            Helper.implode(columnsUpdate, ","),
            this._holderIndex ? valuesUpdate : valuesUpdateHash
        };

        return result;
    }

    public Object[] buildUpdateBatchSql(List<Map<String, Object>> params) {
        return null;
    }

    public Object[] buildSql(Map<String, ?> conditions) {
        String where = "";

        List<Object> params = new ArrayList<Object>();
        List<Object> binding = new ArrayList<Object>();
        Map<String, Object> bindingHash = new HashMap<String, Object>();

        if (conditions.size() < 1) {
            Object[] result = {where, binding};
            return result;
        }

        where = this._getPrefixWhere();

        // Build where clause
        for (Map.Entry<String, ?> row: conditions.entrySet()) {
            String operator = "=";
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
            params.add(column + operator + this._getHolder(column));
            if (this._holderIndex) {
                binding.add(value);
            } else {
                bindingHash.put(column, value);
            }
        }

        // Holder as `?`
        if (this._holderIndex) {
            if (params.size() > 0 && params.size() == binding.size()) {
                where += Helper.implode(params, " AND ");
                Object[] result = {where, binding};
                return result;
            }
        }
        // Holder as `:column`
        else if (params.size() > 0 && params.size() == bindingHash.size()) {
            where += Helper.implode(params, " AND ");
            Object[] result = {where, bindingHash};
            return result;
        }

        Logger.error("[DB][buildSql] => Parameter `conditions` invalid.", this);
        throw new CommonException("[DB][buildSql] => Parameter `conditions` invalid.");
    }
}
