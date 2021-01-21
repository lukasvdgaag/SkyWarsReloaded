SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

CREATE TABLE IF NOT EXISTS `sw_player` (
  `player_id`    INT(6) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uuid`  		VARCHAR(255)    NOT NULL UNIQUE,
  `player_name`	VARCHAR(255)    NOT NULL,
  `wins`        INT(6)          NOT NULL DEFAULT 0,
  `losses`      INT(6)          NOT NULL DEFAULT 0,
  `kills`       INT(6)          NOT NULL DEFAULT 0,
  `deaths`      INT(6)          NOT NULL DEFAULT 0,
  `xp`      	INT(6)          NOT NULL DEFAULT 0,
  `pareffect`	VARCHAR(255)    NOT NULL,
  `proeffect`	VARCHAR(255)    NOT NULL,
  `glasscolor`	VARCHAR(255)    NOT NULL,
  `killsound`	VARCHAR(255)    NOT NULL,
  `winsound`	VARCHAR(255)    NOT NULL,
  `taunt`		VARCHAR(255)    NOT NULL,
  PRIMARY KEY (`player_id`),
  KEY (`uuid`)
)

  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

CREATE TABLE IF NOT EXISTS `sw_permissions` (
  `uuid`  		VARCHAR(255)    NOT NULL,
  `playername`  VARCHAR(60)     NOT NULL,
  `permissions`	VARCHAR(60)     NOT NULL,
  CONSTRAINT `id` PRIMARY KEY (`uuid`, `permissions`),
  KEY (`uuid`)
)

  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;