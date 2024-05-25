package com.perfomax.flexstats.core.contracts

interface UseCaseWithParams <R, P> {
    suspend fun execute(params: P) : R
}


interface UseCaseWithoutParams <R> {
    suspend fun execute() : R
}