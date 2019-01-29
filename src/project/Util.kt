package project

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import project.data.User

val log: Logger = LoggerFactory.getLogger("Contributors")

fun List<User>.aggregate(): List<User> =
    groupingBy { it.login }
        .reduce { login, a, b -> User(login, a.contributions + b.contributions) }
        .values
        .sortedByDescending { it.contributions }
