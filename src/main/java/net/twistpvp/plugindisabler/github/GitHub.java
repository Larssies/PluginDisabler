package net.twistpvp.plugindisabler.github;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GitHub {

    public static String getCommitVersion(Path repoPath) {
        try (Git git = Git.open(repoPath.toFile())) {
            Iterable<RevCommit> logs = git.log().setMaxCount(1).call();
            if (logs.iterator().hasNext()) {
                RevCommit commit = logs.iterator().next();
                return commit.getName().substring(0, 7);
            } else {
                return "unknown";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "unknown";
        } catch (NoHeadException e) {
            e.printStackTrace();
            return "unknown";
        } catch (GitAPIException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

}
