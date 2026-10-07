import { createRouter,createWebHistory } from "vue-router";
import Product from "./views/Product.vue"
import EnterAccaunt from "./views/EnterAccaunt.vue"
import AccauntRegistaration from "./views/Registration_Accaunt.vue"
import Basket from "./views/Basket.vue"
import Accaunt from "./views/Accaunt.vue";
import Catalog from "./views/MainCatalog.vue";
import Vendor_B from "./views/BecomeVendor.vue";
import Create from "./views/CreateProduct.vue";
import Vendor_O from "./views/VendorOrders.vue";
import Vendor_S from "./views/VendorStatistics.vue";
import Vendor_W from "./views/VendorWarehouse.vue";
import Edit_P from "./views/EditProduct.vue";
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
        {path: '/vendor_b', component: Vendor_B},
        {path: '/vendor_o', component: Vendor_O},
        {path: '/vendor_s', component: Vendor_S},
        {path: '/vendor_w', component: Vendor_W},
        {path: '/create_product', component: Create},
        {path: '/edit_product/:id', component: Edit_P}
    ]
})
export default router






