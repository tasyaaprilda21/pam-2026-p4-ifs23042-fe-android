package org.delcom.pam_p4_ifs23042.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),
        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),

        // Bags Routes - TAMBAHAN BARU
        Bags(path = "bags"),
        BagsAdd(path = "bags/add"),
        BagsDetail(path = "bags/{bagId}"),
        BagsEdit(path = "bags/{bagId}/edit"),
    }
}