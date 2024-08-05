package com.closedcircuit.closedcircuitapplication.common.domain.usecase

import app.cash.sqldelight.db.SqlDriver
import com.closedcircuit.closedcircuitapplication.common.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase

class LogoutUseCase(
    private val driver: SqlDriver,
    private val theClosedCircuitDatabase: TheClosedCircuitDatabase,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke() {
        val dropPlanTable = "DROP TABLE IF EXISTS PlanEntity;"
        val dropStepTable = "DROP TABLE IF EXISTS StepEntity;"
        val dropBudgetTable = "DROP TABLE IF EXISTS BudgetEntity;"
        val dropFundRequestTable = "DROP TABLE IF EXISTS FundRequestEntity;"

        driver.execute(null, "PRAGMA foreign_keys = OFF;", 0)
        theClosedCircuitDatabase.transaction {
            driver.execute(null, dropPlanTable, 0)
            driver.execute(null, dropStepTable, 0)
            driver.execute(null, dropBudgetTable, 0)
            driver.execute(null, dropFundRequestTable, 0)
        }
        driver.execute(null, "PRAGMA foreign_keys = ON;", 0).await()

        userRepository.clear()
        sessionRepository.clear()
    }
}