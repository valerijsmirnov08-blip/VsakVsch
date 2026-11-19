import { createRouter,createWebHistory } from "vue-router";
import Product from "./views/Product.vue"
import EnterAccaunt from "./views/EnterAccaunt.vue"
import AccauntRegistaration from "./views/Registration_Accaunt.vue"
import Basket from "./views/Basket.vue"
import Accaunt from "./views/Accaunt.vue";
import Catalog from "./views/MainCatalog.vue";
import Vendor_B from "./views/BecomeVendor.vue";
import Reviews from "./views/Reviews.vue";
import Favorites from "./views/Favorites.vue";
import Delivery from "./views/Delivery.vue";
import VendorDeschBoard from "./views/VendorDeschBoard.vue";
import MessengerView from "./components/MessengerView.vue";
import ChatsHubView from "./components/ChatsHubView.vue";
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
        {path: '/reviews', component: Reviews},
        {path: '/favorites', component:Favorites},
        {path: '/delivery', component:Delivery},
        {path: '/vendor_dash_board', component:VendorDeschBoard},
        {path: '/messenger', component:MessengerView},
        {path: '/chats', component:ChatsHubView}
    ]
})
export default router






