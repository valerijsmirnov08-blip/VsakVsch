import { createRouter,createWebHistory } from "vue-router";
import Product from "./views/Product.vue"
import EnterAccaunt from "./views/EnterAccaunt.vue"
import AccauntRegistaration from "./views/Registration_Accaunt.vue"
import Basket from "./views/Basket.vue"
import Accaunt from "./views/Accaunt.vue";
import Catalog from "./views/MainCatalog.vue";
import Vendor from "./views/BecomeVendor.vue";
import Create from "./views/CreateProduct.vue";

const router = createRouter
({
    history: createWebHistory(),
    routes:
    [
        {path: '/', component: Catalog},
        {path: '/enter_accaunt', component: EnterAccaunt},
        {path: '/accauntR', component: AccauntRegistaration},
        {path: '/basket', component: Basket},
        {path: '/accaunt', component: Accaunt},
        {path: '/product/:id', component: Product},
        {path: '/vendor_b', component: Vendor},
        {path: '/create_product', component: Create}
    ]
})
export default router






