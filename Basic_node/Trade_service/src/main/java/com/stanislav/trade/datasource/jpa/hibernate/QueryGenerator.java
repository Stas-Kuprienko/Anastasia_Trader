package com.stanislav.trade.datasource.jpa.hibernate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

public class QueryGenerator {

    private static final char tableAlias = 't';
    private final StringBuilder strBuilder;


    public QueryGenerator() {
        this.strBuilder = new StringBuilder();
    }


    public QueryGenerator initInsert() {
        strBuilder.append(KeyWord.INSERT);
        return this;
    }

    public QueryGenerator initSelect() {
        strBuilder.append(KeyWord.SELECT);
        return this;
    }

    public QueryGenerator initUpdate() {
        strBuilder.append(KeyWord.UPDATE);
        return this;
    }

    public QueryGenerator initDelete() {
        strBuilder.append(KeyWord.DELETE);
        return this;
    }

    public QueryGenerator fullyFrom() {
        strBuilder.append(' ')
                .append(tableAlias)
                .append(' ')
                .append(KeyWord.FROM);
        return this;
    }

    public QueryGenerator table(Class<?> clas) {
        strBuilder.append(' ')
                .append(clas.getSimpleName())
                .append(' ')
                .append(tableAlias);
        return this;
    }

    public QueryGenerator where(String param) {
        strBuilder.append(' ')
                .append(KeyWord.WHERE).append(' ')
                .append(tableAlias).append('.')
                .append(param).append(' ')
                .append('=').append(' ')
                .append(':').append(param);
        return this;
    }

    public QueryGenerator where(String... parameters) {
        if (parameters.length != 0) {
            strBuilder.append(' ')
                    .append(KeyWord.WHERE).append(' ');
            for (var param : parameters) {
                strBuilder.append(tableAlias).append('.')
                        .append(param).append('=')
                        .append(':').append(param)
                        .append(' ')
                        .append(KeyWord.AND).append(' ');
            }
            int iToDelete = strBuilder.length() - (KeyWord.AND.name().length() + 2);
            strBuilder.delete(iToDelete, strBuilder.length());
        }
        return this;
    }

    public String build() {
        String q = strBuilder.toString();
        strBuilder.setLength(0);
        return q;
    }

    public String nativeSelectAll(Class<?> clas) {
        return String.format(Native.selectFrom.q, getTableName(clas));
    }

    public String nativeSelectAllWhere1Param(Class<?> clas, String param) {
        return String.format(Native.selectFromWhere.q, getTableName(clas), param, param);
    }

    enum KeyWord {
        INSERT, SELECT, FROM, UPDATE, WHERE, SET, DELETE, AND
    }

    enum Native {
        selectFrom("SELECT * FROM %s;"),
        selectFromWhere("SELECT * FROM %s WHERE %s = :%s;");

        final String q;

        Native(String q) {
            this.q = q;
        }
    }

    private String getTableName(Class<?> clas) {
        if (clas.getAnnotation(Entity.class) != null) {
            Table table = clas.getAnnotation(Table.class);
            if (table != null) {
                return table.name();
            } else {
                return clas.getSimpleName();
            }
        } else {
            throw new IllegalArgumentException('\'' + clas.getName() + "' is not @Entity annotated class.");
        }
    }
}
