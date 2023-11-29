package com.dicoding.picodiploma.StoryApp

import com.dicoding.picodiploma.StoryApp.data.api.ListStoryItem

object DataDummy {

    fun generateDummyStories(): List<ListStoryItem> {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20) {
            val story = ListStoryItem(
                photoUrl = "0",
                createdAt = "",
                name = "Name $i",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
            )
            listStory.add(story)
        }

        return listStory
    }
}