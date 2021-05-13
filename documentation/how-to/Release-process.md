# Creating a release

For convenience, we use `git flow`. You can find the tool and more information about the process at <https://danielkummer.github.io/git-flow-cheatsheet/>.

*Note: In the following steps, just replace `0.2.0` by the new version number.*

## Step 1: Start release

```console
$ git checkout develop
$ git pull
$ git flow release start 0.2.0
```

## Step 2: Test

* Bump version number in `pom.xml` and commit change.
* Check if everything's fine and fix issues if necessary.

## Step 3: Finish release

```console
$ git flow release finish '0.2.0' # When asked, enter `0.2.0` as message for tag
$ git push origin master
$ git push origin develop
$ git push origin --tags
```

[This wiki page was originally created by Thomas Oberroither (@csaw3618)]