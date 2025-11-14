package com.ethereal.home.components.bottomSheet.slide_navigation

enum class PlannerStep { Location, Radius, Cuisine, Details }

val PlannerFlow = listOf(
    PlannerStep.Location,
    PlannerStep.Radius,
    PlannerStep.Cuisine,
    PlannerStep.Details
)

fun PlannerStep.index(): Int = PlannerFlow.indexOf(this)
fun PlannerStep.nextOrSelf(): PlannerStep =
    PlannerFlow.getOrNull(index() + 1) ?: this

fun PlannerStep.prevOrSelf(): PlannerStep =
    PlannerFlow.getOrNull(index() - 1) ?: this