#!/bin/bash

export GIT_REPO_URL="$GIT_REPO_URL"
git clone "$GIT_REPO_URL" /home/app/workspace
exec node script.js