# Detailed Report

https://arxiv.org/abs/2407.11934

# Directory Structure

- `Intellij-Plugin`: This directory contains the Intellij Plugin frontend.

# Notes

Update directly to the `main` branch but only modify either this `README.md` or your own folder.

# Deliverables

Every 2 weeks:

* Specification Document Update
* Design Document Update

# AI Integration

Uses Mistral 7b as LLM, found here: https://github.com/mistralai/mistral-src

# Specification

## General Description

The Code Helper and Comment Tracker, or CHCT, is a tool designed to be used as an interface between the user and a backend machine learning engine. The CHCT will assist the user in assessing highlighted code or comments for improvements or viability. In addition, the CHCT will associate and track comment changes across a file via `comGen.py`. 

## Product Scope

The CHCT will accomplish the following objectives:

1. Provide the user with feedback on suggest code implementations based on a highlighted comment.
2. Track comments and modify them across a file using a root file to hold all comments.

## Product Value

This product would be useful in helping software developers develop reliable code through code suggestions and comment association. 

## Intended Audience

The intended audience for this program would be software developers and engineers.

## Intended Use

The intended use is as an Machine Learning interface and general program helper.

