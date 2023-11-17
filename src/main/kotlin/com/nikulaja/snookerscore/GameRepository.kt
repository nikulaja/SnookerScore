package com.nikulaja.snookerscore

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface GameRepository : CrudRepository<Game, UUID>
