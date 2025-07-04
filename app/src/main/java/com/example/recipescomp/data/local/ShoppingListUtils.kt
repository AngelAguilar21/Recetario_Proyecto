package com.example.recipescomp.data.local

suspend fun addRecipeToShoppingListUniversal(
    dao: ShoppingListDao,
    mealId: String,
    name: String,
    imageUrl: String,
    ingredients: String,
    userId: String // ✅ ADDED: userId parameter
) {
    val safeMealId = mealId.trim()
    if (safeMealId.isBlank()) return

    // ✅ FIXED: Pass userId to check for existing items
    val existing = dao.getItemByMealId(safeMealId, userId)

    if (existing != null) {
        dao.updateItem(existing.copy(quantity = existing.quantity + 1))
    } else {
        dao.insertItem(
            ShoppingItemEntity(
                mealId = safeMealId,
                name = name,
                imageUrl = imageUrl,
                ingredients = ingredients,
                quantity = 1,
                userId = userId // ✅ FIXED: Set the userId
            )
        )
    }
}