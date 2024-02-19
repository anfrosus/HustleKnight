package com.woozy.untitled.dto.request

import com.woozy.untitled.model.enums.ItemUpgradeCategory

data class UpgradeRequest(
    //TODO: param으로 받을까?
    val upgradeDetail: ItemUpgradeCategory
) {

}
