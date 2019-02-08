package project

import project.data.RequestData
import project.data.User
import project.data.createGitHubService
import kotlin.concurrent.thread

fun loadContributorsBlocking(req: RequestData): List<User> {
    val service = createGitHubService(req.username, req.password)

    log.info("Loading ${req.org} repos")
    val repos = service.listOrgRepos(req.org).responseBodyBlocking()

    log.info("${req.org}: loaded ${repos.size} repos")

    val contribs = repos.flatMap { repo ->
        val users = service.listRepoContributors(req.org, repo.name).responseBodyBlocking()
        log.info("${repo.name}: loaded ${users.size} contributors")
        users
    }.aggregate()

    log.info("Total: ${contribs.size} contributors")
    return contribs
}

fun loadContributorsBackground(req: RequestData, callback: (List<User>) -> Unit) {
    thread {
        val users = loadContributorsBlocking(req)
        callback(users)
    }
}