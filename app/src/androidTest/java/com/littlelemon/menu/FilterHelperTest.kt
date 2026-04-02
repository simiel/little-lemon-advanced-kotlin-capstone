import org.junit.Test
import org.junit.Assert.*

class FilterHelperTest {

    @Test
    fun filterProducts_filterTypeDessert_croissantReturned() {

        val sampleProductsList = listOf(
            ProductItem("Black tea", 3.00, "Drinks", R.drawable.black_tea),
            ProductItem("Croissant", 7.00, "Dessert", R.drawable.croissant),
            ProductItem("Bouillabaisse", 20.00, "Food", R.drawable.bouillabaisse)
        )

        val result = FilterHelper.filterProducts(
            FilterType.Dessert,
            sampleProductsList
        )

        assertEquals(1, result.size)
        assertEquals("Croissant", result[0].title)
    }
}