package com.hustleknight.app.dto.request

import com.hustleknight.app.model.enums.ItemUpgradeCategory

data class UpgradeRequest(
    //TODO: param으로 받을까?
    val upgradeDetail: ItemUpgradeCategory
) {

}
