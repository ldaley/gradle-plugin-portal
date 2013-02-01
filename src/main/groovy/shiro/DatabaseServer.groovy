package shiro

import groovy.sql.Sql

class DatabaseServer {

    void start() {
        def sql = Sql.newInstance("jdbc:h2:file:data", "sa", "", "org.h2.Driver")
        sql.execute("drop table if exists user")
        sql.execute("create table user (user VARCHAR primary key, password VARCHAR)")
        sql.execute("insert into user values ('db1', 'password'), ('db2', 'password')")
    }

}
