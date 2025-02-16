package com.example.data.table

import org.jetbrains.exposed.sql.Table

object CrmTable:Table() {

    val id = varchar("id",512)
    val userMail = varchar("userMail",512).references(UserTable.email)
    val mail = varchar("mail", 512)
    val position = varchar("position", 512)
    val telephone = varchar("telephone", 512)
    val telephone2 = varchar("telephone2", 512)
    val company = varchar("company", 512)
    val sites = varchar("sites", 512)
    val typeActions = varchar("typeActions", 512)
    val typeMaterial = varchar("typeMaterial", 512)
    val address = varchar("address", 512)
    val notes = text("notes")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}