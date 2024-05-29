package com.patri.nutriplanner

import com.patri.nutriplanner.database.entities.FoodState
import com.patri.nutriplanner.database.entities.FoodType

class FoodProvider {
    companion object {
        fun getFood(): List<Food> {
            return food
        }

        private val food: List<Food> = listOf(
            Food(
                name = "Lechuga",
                type = FoodType.VERDURAS.name,
                image = "https://ecohuertamanchega.es/wp-content/uploads/2022/01/23-1.png",
                state = FoodState.SHOPPING.stateName
            ),
            Food(
                name = "Manzana Fuji",
                type = FoodType.FRUTAS.name,
                image = "https://comefruta.es/wp-content/uploads/manzana-fuji.jpg",
                state = FoodState.PANTRY.stateName
            ),
            Food(
                name = "Almendras",
                type = FoodType.FRUTOSSECOS.name,
                image = "https://www.dosfarma.com/blog/wp-content/uploads/2018/04/26-03-2018-almendras.jpg",
                state = FoodState.SHOPPING.stateName
            ),
            Food(
                name = "Avellanas",
                type = FoodType.FRUTOSSECOS.name,
                image = "https://assets.elgourmet.com/wp-content/uploads/2023/03/diccio_1736.jpg.webp",
                state = FoodState.SHOPPING.stateName
            ),
            Food(
                name = "Naranjas",
                type = FoodType.FRUTAS.name,
                image = "https://elpoderdelconsumidor.org/wp-content/uploads/2022/02/naranja-1.jpg",
                state = FoodState.PANTRY.stateName
            ),
            Food(
                name = "Fresas",
                type = FoodType.FRUTAS.name,
                image = "https://dialprix.es/blog/wp-content/uploads/fresas.jpg",
                state = FoodState.NULL.stateName
            ),
            Food(
                name = "Pechuga de pollo",
                type = FoodType.CARNE.typeName,
                image = "https://i0.wp.com/www.domiciliohavana.com/wp-content/uploads/2024/01/domiclio-havana-blog-pechuga-pollo-receta-suprema.jpg",
                state = FoodState.SHOPPING.name
            ),
            Food(
                name = "Zanahorias",
                type = FoodType.VERDURAS.name,
                image = "https://www.fruteriaelvergel.com/WebRoot/StoreES2/Shops/64945569/572E/4288/2CF8/DF8E/766F/C0A8/2BBA/C2CA/2021-zanahoria-el-vergel-cantabro.jpg",
                state = FoodState.NULL.name
            ),
            Food(
                name = "Plátanos",
                type = FoodType.FRUTAS.name,
                image = "https://www.quimicaysociedad.org/wp-content/uploads/2015/06/platano.jpg",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Cerezas",
                type = FoodType.FRUTAS.name,
                image = "https://super-masymas.com/wp-content/uploads/2022/07/580b57fcd9996e24bc43c13f.png",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Queso parmesano",
                type = FoodType.LACTEOS.name,
                image = "https://m.media-amazon.com/images/I/51OEAt5sN5L._AC_UF894,1000_QL80_.jpg",
                state = FoodState.SHOPPING.name
            ),
            Food(
                name = "Yogurt griego",
                type = FoodType.LACTEOS.name,
                image = "https://acdn.mitiendanube.com/stores/001/129/542/products/kay-61-009099cef73678dbde16854714613982-480-0.jpg",
                state = FoodState.SHOPPING.name
            ),
            Food(
                name = "Melón",
                type = FoodType.FRUTAS.name,
                image = "https://www.agroponiente.com/wp-content/uploads/2023/08/melon-negro-piel-de-sapo-Agroponiente.png",
                state = FoodState.SHOPPING.name
            ),
            Food(
                name = "Cebollas",
                type = FoodType.VERDURAS.name,
                image = "https://ramadadelivery.cl/wp-content/uploads/2021/03/cebolla.jpg",
                state = FoodState.SHOPPING.name
            ),
            Food(
                name = "Uva blanca",
                type = FoodType.FRUTAS.name,
                image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEIWzRZGmtt0e60xEz8wFIXWxcJHwfEMlI_ykvDc6GKQ&s",
                state = FoodState.SHOPPING.name
            ),
            Food(
                name = "Aguacate",
                type = FoodType.FRUTAS.name,
                image = "https://thefoodtech.com/wp-content/uploads/2023/07/aguacate-828x548.jpg",
                state = FoodState.SHOPPING.name
            ),
            Food(
                name = "Piña",
                type = FoodType.FRUTAS.name,
                image = "https://www.gob.mx/cms/uploads/image/file/415269/pi_a_1.jpg",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Salmón",
                type = FoodType.PESCADO.name,
                image = "https://opercebeiro.com/wp-content/uploads/nc/catalog/rodaja-salmon-opercebeiro-800x800.png",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Remolacha",
                type = FoodType.VERDURAS.name,
                image = "https://www.sportlife.es/uploads/s1/12/68/64/90/remolacha-una-hortaliza-con-propiedades-de-superalimento.jpeg",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Carne picada",
                type = FoodType.CARNE.name,
                image = "https://i0.wp.com/meatlovers.es/wp-content/uploads/carne-picada-ternera-burger-meat-ternera_810050-1-scaled.jpg",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Huevos",
                type = FoodType.HUEVOS.name,
                image = "https://avinews.com/wp-content/uploads/2021/04/HUEVOS-830X300.jpg",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Lomo de cerdo",
                type = FoodType.CARNE.name,
                image = "https://www.carniceriayerro.com/251-large_default/lomo-de-cerdo.jpg",
                state = FoodState.PANTRY.name
            ),
            Food(
                name = "Jamón serrano",
                type = FoodType.CARNE.name,
                image = "https://www.asantasede.com/cdn/shop/products/racionjamonserrano_670x.jpg",
                state = FoodState.PANTRY.name
            )
        )
    }
}