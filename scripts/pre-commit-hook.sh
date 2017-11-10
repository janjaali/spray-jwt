#!/bin/bash

# Checks if locally staged changes are formatted properly ignoring non-staged changes.
# @see: https://gist.github.com/cvogt/2676ed6c6d1abafa3d6a

PATH=$PATH:/usr/local/bin:/usr/local/sbin

echo ""
echo "Doing some Checks…"

echo "* Moving to the project directory…"
_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
DIR=$( echo $_DIR | sed 's/\/.git\/hooks$//' )

echo "* Stashing non-staged changes so we don't check them"…
git diff --quiet
hadNoNonStagedChanges=$?

if ! [ $hadNoNonStagedChanges -eq 0 ]
then
    git stash --keep-index -u > /dev/null
fi

echo "* Compiling…"
sbt test:compile > /dev/null
compiles=$?

if [ $compiles -ne 0 ]
then
    echo "  [KO] Error compiling "
else
    echo "* Checking production code style…"

    sbt scalastyle > /dev/null
    productionScalastyle=$?

    if [ $productionScalastyle -ne 0 ]
    then
        echo "  [KO] Error checking production code style"
    else
        echo "* Checking test code style…"

        sbt test:scalastyle > /dev/null
        testScalastyle=$?

        if [ $testScalastyle -ne 0 ]
        then
            echo "  [KO] Error checking test code style"
        fi
    fi
fi

echo "* Applying the stash with the non-staged changes…"
if ! [ $hadNoNonStagedChanges -eq 0 ]
then
    sleep 1 && git stash pop --index > /dev/null & # sleep because otherwise commit fails when this leads to a merge conflict
fi

# Final result
echo ""

if [ $compiles -eq 0 ] && [ $productionScalastyle -eq 0 ] && [ $testScalastyle -eq 0 ]
then
    echo "[OK] Your code will be committed young padawan"
    exit 0
elif [ $compiles -ne 0 ]
then
    echo "[KO] Cancelling commit due to compile error (run 'sbt test:compile' for more information)"
    exit 1
elif [ $productionScalastyle -ne 0 ]
then
    echo "[KO] Cancelling commit due to production code style error (run 'sbt scalastyle' for more information)"
    exit 2
else
    echo "[KO] Cancelling commit due to test code style error (run 'sbt test:scalastyle' for more information)"
    exit 3
fi