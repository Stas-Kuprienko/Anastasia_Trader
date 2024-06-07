package com.stanislav.trade.datasource.hibernate;

public class QueryGenerator {

    enum KeyWord {
        SELECT, FROM, UPDATE, WHERE, SET, DELETE
    }

    enum Native {
        selectFrom("SELECT * FROM %s;"),
        selectFromWhere("SELECT * FROM %s WHERE %s = :%s;");

        final String q;

        Native(String q) {
            this.q = q;
        }
    }
}
