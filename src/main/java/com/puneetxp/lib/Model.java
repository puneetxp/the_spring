package com.puneetxp.lib;

import java.util.*;

public abstract class Model {
    protected String table;
    protected String name;
    protected Map<String, Map<String, Object>> relations = new HashMap<>(); // Simplified for example
    protected List<Map<String, Object>> items = new ArrayList<>();

    // DB Interface dummy
    public interface DB {
        void rawSql(String sql);

        void setPlaceholders(List<Object> placeholders);

        List<Map<String, Object>> many();
    }

    protected DB db;

    public Model(DB db) {
        this.db = db;
    }

    public Model join(Map<String, Object> joins, Map<String, Object> where) {
        List<SqlBuilder.JoinSpec> joinSpecs = new ArrayList<>();

        for (Map.Entry<String, Object> entry : joins.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();

            String relationName = (key.matches("\\d+")) ? (String) val : key;
            // Simple logic for extraction

            if (relations.containsKey(relationName)) {
                Map<String, Object> r = relations.get(relationName);
                SqlBuilder.JoinSpec spec = new SqlBuilder.JoinSpec();
                spec.alias = relationName;
                spec.table = (String) r.get("table");
                spec.localKey = (String) r.get("name");
                spec.foreignKey = (String) r.get("key");
                spec.cols = new ArrayList<>(); // Empty default
                spec.prefix = relationName;

                joinSpecs.add(spec);
            }
        }

        SqlBuilder.JoinQueryResult query = SqlBuilder.buildJoinQuery(
                this.table, this.table, new ArrayList<>(), joinSpecs, where);

        this.db.rawSql(query.sql);
        this.db.setPlaceholders(query.placeholders);
        this.items = this.db.many();

        return this;
    }
}
