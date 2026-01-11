package com.puneetxp.lib;

import java.util.*;
import java.util.stream.Collectors;

public class SqlBuilder {

    public static class JoinSpec {
        public String alias;
        public String table;
        public String localKey;
        public String foreignKey;
        public List<String> cols;
        public String prefix;
    }

    public static class JoinQueryResult {
        public String sql;
        public List<Object> placeholders;
        public List<String> selectParts;

        public JoinQueryResult(String sql, List<Object> placeholders, List<String> selectParts) {
            this.sql = sql;
            this.placeholders = placeholders;
            this.selectParts = selectParts;
        }
    }

    public static List<String> buildSelectList(String alias, List<String> cols, String prefix) {
        if (cols == null || cols.isEmpty()) {
            return Collections.singletonList(alias + ".*");
        }
        return cols.stream().map(col -> {
            String as = (prefix != null && !prefix.isEmpty()) ? prefix + "__" + col : col;
            return alias + ".`" + col + "` AS " + as;
        }).collect(Collectors.toList());
    }

    public static JoinQueryResult buildJoinQuery(
            String baseTable,
            String baseAlias,
            List<String> baseCols,
            List<JoinSpec> joins,
            Map<String, Object> where
    ) {
        List<String> selectParts = new ArrayList<>(buildSelectList(baseAlias, baseCols, null));
        StringBuilder joinSql = new StringBuilder();

        for (JoinSpec join : joins) {
            List<String> relSelect = buildSelectList(join.alias, join.cols, join.prefix);
            selectParts.addAll(relSelect);
            joinSql.append(String.format(" LEFT JOIN %s %s ON %s.`%s` = %s.`%s`",
                    join.table, join.alias, baseAlias, join.localKey, join.alias, join.foreignKey));
        }

        List<String> whereClauses = new ArrayList<>();
        List<Object> placeholders = new ArrayList<>();

        if (where != null) {
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                if (entry.getValue() instanceof List) {
                    List<?> list = (List<?>) entry.getValue();
                    if (!list.isEmpty()) {
                        String qs = list.stream().map(i -> "?").collect(Collectors.joining(","));
                        whereClauses.add(String.format("%s.`%s` IN (%s)", baseAlias, entry.getKey(), qs));
                        placeholders.addAll(list);
                    }
                } else {
                    whereClauses.add(String.format("%s.`%s` = ?", baseAlias, entry.getKey()));
                    placeholders.add(entry.getValue());
                }
            }
        }

        String whereSql = "";
        if (!whereClauses.isEmpty()) {
            whereSql = " WHERE " + String.join(" AND ", whereClauses);
        }

        String sql = String.format("SELECT %s FROM %s %s%s%s",
                String.join(", ", selectParts), baseTable, baseAlias, joinSql.toString(), whereSql);

        return new JoinQueryResult(sql, placeholders, selectParts);
    }
}
