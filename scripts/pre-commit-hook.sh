#!/bin/bash

echo ""
echo "Doing some Checks…"
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
    echo "* Checking code style…"

    sbt scalastyle > /dev/null
    productionScalastyle=$?

    if [ $productionScalastyle -ne 0 ]
    then
        echo "  [KO] Error checking code style"
    fi
fi

echo "* Applying the stash with the non-staged changes…"
if ! [ $hadNoNonStagedChanges -eq 0 ]
then
    sleep 1 && git stash pop --index > /dev/null & # sleep because otherwise commit fails when this leads to a merge conflict
fi

# Final result
echo ""

if [ $compiles -eq 0 ] && [ $productionScalastyle -eq 0 ]
then
    echo "[OK] Your code will be committed young padawan"
    exit 0
elif [ $compiles -ne 0 ]
then
    echo "[KO] Cancelling commit due to compile error (run 'sbt test:compile' for more information)"
    exit 1
elif [ $productionScalastyle -ne 0 ]
then
    echo "[KO] Cancelling commit due to code style error (run 'sbt scalastyle' for more information)"
    exit 2
fi
