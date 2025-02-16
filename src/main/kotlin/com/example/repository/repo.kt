package com.example.repository

import com.example.data.model.Crm
import com.example.data.model.User
import com.example.data.table.CrmTable
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class Repo {

    suspend fun addUser(user:User){
        dbQuery{
            UserTable.insert { ut->
                ut[UserTable.email] = user.email
                ut[UserTable.hashPassword] = user.hashPassword
                ut[UserTable.name] = user.userName
            }
        }
    }

    suspend fun findUserByEmail(email:String) = dbQuery {
        UserTable.selectAll().where { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row:ResultRow?):User?{
        if(row == null){
            return null
        }

        return User(
            email =  row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.name]
        )
    }


// ========== CRM =========

suspend fun addCrm(crm: Crm, email: String){

    dbQuery {
        CrmTable.insert { nt ->
            nt[CrmTable.id] = crm.id
            nt[CrmTable.userMail] = email
            nt[CrmTable.mail] = crm.mail
            nt[CrmTable.position] = crm.position
            nt[CrmTable.telephone] = crm.telephone
            nt[CrmTable.telephone2] = crm.telephone2
            nt[CrmTable.company] = crm.company
            nt[CrmTable.sites] = crm.sites
            nt[CrmTable.typeActions] = crm.typeActions
            nt[CrmTable.typeMaterial] = crm.typeMaterial
            nt[CrmTable.address] = crm.address
            nt[CrmTable.notes] = crm.notes
        }
    }
}
    suspend fun getAllCrms(email: String):List<Crm> = dbQuery {
        CrmTable.selectAll().where { CrmTable.userMail eq email }

    }.mapNotNull { rowToCrm(it) }

suspend fun updateCrm(crm: Crm, email: String){
    dbQuery {
        CrmTable.update (
            where = {
                CrmTable.userMail.eq(email) and CrmTable.id.eq(crm.id)
            }
        ){ nt->
            nt[CrmTable.mail] = crm.mail
            nt[CrmTable.position] = crm.position
            nt[CrmTable.telephone] = crm.telephone
            nt[CrmTable.telephone2] = crm.telephone2
            nt[CrmTable.company] = crm.company
            nt[CrmTable.sites] = crm.sites
            nt[CrmTable.typeActions] = crm.typeActions
            nt[CrmTable.typeMaterial] = crm.typeMaterial
            nt[CrmTable.address] = crm.address
            nt[CrmTable.notes] = crm.notes

        }
    }
}

suspend fun deleteCrm(id:String,email: String){
    dbQuery {
        CrmTable.deleteWhere { CrmTable.userMail.eq(email) and CrmTable.id.eq(id) }
    }
}

private fun rowToCrm(row:ResultRow?):Crm?{
    if(row == null){
        return null
    }
    return Crm(
        id = row[CrmTable.id],
        mail = row[CrmTable.mail],
        position = row[CrmTable.position],
        telephone = row[CrmTable.telephone],
        telephone2 = row[CrmTable.telephone2],
        company = row[CrmTable.company],
        sites = row[CrmTable.sites],
        typeActions = row[CrmTable.typeActions],
        typeMaterial = row[CrmTable.typeMaterial],
        address = row[CrmTable.address],
        notes = row[CrmTable.notes],
    )
}

}