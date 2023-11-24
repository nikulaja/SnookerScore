package com.nikulaja.snookerscore.integration

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegrationTestBase {

    @Autowired
    lateinit var dataSource: DataSource

    @BeforeEach
    fun setUp() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute(
                    "CREATE TABLE IF NOT EXISTS game (\n" +
                        "    id UUID PRIMARY KEY,\n" +
                        "    player1name VARCHAR(30) NOT NULL,\n" +
                        "    player2name VARCHAR(30) NOT NULL,\n" +
                        "    ongoing BOOLEAN NOT NULL,\n" +
                        "    player1score INTEGER DEFAULT 0,\n" +
                        "    player2score INTEGER DEFAULT 0,\n" +
                        "    timestamp TIMESTAMP NOT NULL\n" +
                        ");\n",
                )
            }
        }
    }

    @AfterEach
    fun tearDown() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("DROP TABLE game")
            }
        }
    }

    fun setTestData() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute(
                    "INSERT INTO game " +
                        "(player1name, player2name, ongoing, player1score, player2score, timestamp, id) VALUES " +
                        "('Player 1', 'Player 2', true, 0, 0, '2022-01-01T00:00:00Z', " +
                        "'123e4567-e89b-12d3-a456-426655440000')",
                )
                statement.execute(
                    "INSERT INTO game " +
                        "(player1name, player2name, ongoing, player1score, player2score, timestamp, id) VALUES " +
                        "('Player 3', 'Player 4', false, 5, 10, '2022-01-02T00:00:00Z', " +
                        "'234e5678-f90a-1b2c-d345-678901234567')",
                )
            }
        }
    }
}
