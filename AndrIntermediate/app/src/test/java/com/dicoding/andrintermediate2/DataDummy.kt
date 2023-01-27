package com.dicoding.andrintermediate2

import com.dicoding.andrintermediate2.response.*

object DataDummy {

    fun generateDummyStoryList(): List<ListStory> {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 0..10) {
            val story = ListStory(
                i.toString(),
                "created + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                "id + $i",
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyLogin(): LoginResponse {
        return LoginResponse(
            LoginResult(
                "name",
                "id",
                "token"
            ),
            false,
            "token"
        )
    }

    fun generateDummyRegister(): RegisterResponse {
        return RegisterResponse(
            false,
            "success"
        )
    }

    fun generateDummyStoryWithMaps(): AllStoryResponse {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 0..10) {
            val story = ListStory(
                i.toString(),
                "created + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                "id + $i",
                i.toDouble()
            )
            items.add(story)
        }
        return AllStoryResponse(
            items,
            false,
            "success"
        )
    }

    fun generateDummyAddNewStory(): NewStoryResponse {
        return NewStoryResponse(
            false,
            "success"
        )
    }
}