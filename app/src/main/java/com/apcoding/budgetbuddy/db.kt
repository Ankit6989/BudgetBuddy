package com.apcoding.budgetbuddy

import com.apcoding.budgetbuddy.models.Category
import com.apcoding.budgetbuddy.models.Expense
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val config = RealmConfiguration.create(schema = setOf(Expense::class, Category::class))
val db: Realm = Realm.open(config)