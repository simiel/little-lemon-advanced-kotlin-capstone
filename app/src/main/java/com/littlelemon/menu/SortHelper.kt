package com.littlelemon.menu

class SortHelper {

    fun sortProducts(type: SortType, productsList: List<ProductItem>): List<ProductItem> {
        return when (type) {
            SortType.Alphabetically -> productsList.sortAlphabetically()
            SortType.PriceAsc -> productsList.sortByPriceAsc()
            SortType.PriceDesc -> productsList.sortByPriceDesc()
            else -> productsList
        }
    }

    private fun List<ProductItem>.sortAlphabetically(): List<ProductItem> {
        return sortedBy { it.title }
    }

    private fun List<ProductItem>.sortByPriceAsc(): List<ProductItem> {
        return this.sortedBy { it.price }
    }

    private fun List<ProductItem>.sortByPriceDesc(): List<ProductItem> {
        return this.sortedByDescending { it.price }
    }
}