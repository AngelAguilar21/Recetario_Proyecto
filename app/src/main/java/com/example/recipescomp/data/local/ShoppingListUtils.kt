package com.example.recipescomp.data.local

suspend fun addRecipeToShoppingListUniversal(
    dao: ShoppingListDao,
    mealId: String,
    name: String,
    imageUrl: String,
    ingredients: String
) {
    // ✅ NORMALIZAR el mealId de forma consistente
    val safeMealId = mealId.trim()
    if (safeMealId.isBlank()) return

    // 🔍 Buscar si ya existe la receta
    val existing = dao.getItemByMealId(safeMealId)

    if (existing != null) {
        // ✅ Si existe, incrementar cantidad
        dao.updateItem(existing.copy(quantity = existing.quantity + 1))
    } else {
        // ✅ Si no existe, crear nueva entrada
        dao.insertItem(
            ShoppingItemEntity(
                mealId = safeMealId,
                name = name,
                imageUrl = imageUrl,
                ingredients = ingredients,
                quantity = 1
            )
        )
    }
}