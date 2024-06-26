package com.jetbrains.kmpapp.screens.list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jetbrains.kmpapp.data.MuseumObject
import com.jetbrains.kmpapp.data.MuseumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ListScreenModel(museumRepository: MuseumRepository) : ScreenModel {
    val objects: StateFlow<List<MuseumObject>> =
        museumRepository.getObjects()
            .map { objects ->
                // make the number of objects 50 times larger
                val newObjects = objects.toMutableList()
                repeat(50) { newObjects.addAll(objects) }
                newObjects.mapIndexed { index, obj ->
                    obj.copy(
                        objectID = obj.objectID + index,
                    )
                }
                    // require all objects to have unique IDs
                    .distinctBy { it.objectID }
            }
            .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
