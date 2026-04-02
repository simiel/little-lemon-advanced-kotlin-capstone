package com.littlelemon.menu

class FilterHelper {//TODO create a FilterHelperTest and write a unit test for filterProducts

    fun filterProducts(type: FilterType, productsList: List<ProductItem>): List<ProductItem> {
        return when (filterType) {
            FilterType.All -> productsList
            else -> productsList.filter {
                it.category == filterType.name
            }
        }
    }

}