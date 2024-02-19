package com.woozy.untitled.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val httpStatus: HttpStatus,
    val message: String
) {
    PLAYER_NOT_FOUND("P001", HttpStatus.NOT_FOUND, "PLAYER 를 찾을 수 없습니다."),
    PLAYER_NOT_MATCH("P002", HttpStatus.BAD_REQUEST, "PLAYER 가 일치하지 않습니다."),
    PLAYER_ILLEGAL_STAGE("P003", HttpStatus.BAD_REQUEST, "도전할 수 없는 스테이지 입니다."),
    PLAYER_EMAIL_ALREADY_EXIST("P004", HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    PLAYER_PASSWORD_NOT_MATCH("P005", HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PLAYER_GOODS_NOT_FOUND("P006", HttpStatus.NOT_FOUND, "플레이어 재화를 찾을 수 없습니다."),
    PLAYER_GOODS_NOT_ENOUGH("P007", HttpStatus.UNPROCESSABLE_ENTITY, "플레이어 재화가 부족합니다."),
    PLAYER_LEVEL_NOT_MET("P008", HttpStatus.UNPROCESSABLE_ENTITY, "플레이어 레벨이 부족합니다."),
    PLAYER_STATS_CAN_NOT_MINUS("P009", HttpStatus.BAD_REQUEST, "플레이어의 스텟이 음수가 될 수 없습니다.."),
    PLAYER_NOT_ENOUGH_TICKET("P010", HttpStatus.UNPROCESSABLE_ENTITY, "플레이어의 티켓이 모자랍니다."),

    ITEM_NOT_FOUND("I001", HttpStatus.NOT_FOUND, "ITEM 을 찾을 수 없습니다."),
    ITEM_EQUIPPED("I002", HttpStatus.BAD_REQUEST, "아이템을 해제한 후 시도해 주세요."),
    ITEM_NOT_EQUIPPED("I003", HttpStatus.BAD_REQUEST, "아이템이 장착되어있지 않습니다."),
    ITEM_ZERO_REMAINING("I004", HttpStatus.BAD_REQUEST, "아이템 강화 횟수가 남아있지 않습니다."),
    ITEM_CATEGORY_NOT_MATCH("U005", HttpStatus.BAD_REQUEST, "아이템 카테고리가 일치하지 않습니다."),

    MONSTER_NOT_FOUND("M001", HttpStatus.NOT_FOUND, "MONSTER 를 찾을 수 없습니다."),
    MONSTER_HP_ZERO("M002", HttpStatus.UNPROCESSABLE_ENTITY, "MONSTER 가 이미 처치되었습니다."),

    EMITTER_NOT_FOUND("E001", HttpStatus.NOT_FOUND, "해당 PLAYER 의 EMITTER 를 찾을 수 없습니다."),
    EMITTER_ALREADY_EXIST("E002", HttpStatus.CONFLICT, "해당 PLAYER 의 EMITTER 가 이미 존재합니다."),

    REDIS_BATTLE_NOT_FOUND("R001", HttpStatus.NOT_FOUND, "전투정보를 찾을 수 없습니다."),
    REDIS_CAN_NOT_SAVE("R002", HttpStatus.INTERNAL_SERVER_ERROR, "REDIS 저장에 실패했습니다."),
    REDIS_BATTLE_NOT_COMPLETE("R003", HttpStatus.CONFLICT, "전투가 진행중입니다."),
    REDIS_EMITTER_NOT_FOUND("R004", HttpStatus.NOT_FOUND, "REDIS 에서 Emitter 를 찾을 수 없습니다."),
    REDIS_EMITTER_ALREADY_EXIST("R004", HttpStatus.NOT_FOUND, "REDIS 에서 Emitter 가 이미 존재합니다."),


    UNAUTHORIZED("A001", HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN("A002", HttpStatus.FORBIDDEN, "권한이 없습니다.")
    ;
}