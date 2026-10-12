# SWEN20003 Semester 2, 2026
# Project 2
# Mokepon

## Assumptions

* Relative data and image paths are resolved from the application's working directory.
* Clicking the currently selected Mokepon again keeps it selected.
* Every strong or weak marker has at least one eligible Mokepon in the loaded data. If
  no eligible type exists, an explicit error is raised instead of selecting an incorrect type.
* Entering a marker selects its opponent again, including when revisiting after running away.
  Each eligible list entry has equal probability. Enemy skills are chosen uniformly from
  available, affordable entries.
* A turn for stun/blindness means an action by the affected fighter. An effect inflicted
  after that fighter has acted begins counting with its next action. The effect lasts
  through the end-of-round bleeding calculation for the third affected action.
* Reapplying bleeding, stun or blindness refreshes its duration instead of stacking it.
  Bleeding starts at the end of the round in which Lance hits and uses the attacker's
  current strength, its stun modifier, and the target's current defence.
* If a mystery box shares a tile with another object, the box is collected first.
  An encounter on an exit tile is resolved before the exit is activated after victory.
* Modal dialogs block gameplay and menu navigation until one of their buttons is selected.
  Closing the window remains available.

## AI Statement

I acknowledge using OpenAI ChatGPT to help interpret the project specification, explain Java and JavaFX concepts, generate and revise parts of the implementation, compare the program with the project requirements, and diagnose errors. I reviewed and understood the final implementation myself.

## Code References

The supplied Project 1 skeleton code and Project 2 materials.
SWEN20003 lecture materials



