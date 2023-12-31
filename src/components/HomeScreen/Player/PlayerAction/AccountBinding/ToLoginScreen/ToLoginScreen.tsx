import { View, Text } from "react-native";
import React from "react";
import { Server } from "../../../../../../utils/hoyolab/servers/hsrServer.types";
import useHsrServerChosen from "../../../../../../redux/hsrServerChosen/useHsrServerChosen";
import { useNavigation } from "@react-navigation/native";
import Button from "../../../../../global/Button/Button";
import { SCREENS } from "../../../../../../constant/screens";
import TextButton from "../../../../../global/TextButton/TextButton";

const hsrServers: Server[] = [
  { id: "cn1", name: "星穹列车", platform: "mihoyo" },
  { id: "cn2", name: "无名客", platform: "mihoyo" },
  { id: "asia", name: "Asia", platform: "hoyolab" },
  { id: "europe", name: "Europe", platform: "hoyolab" },
  { id: "america", name: "America", platform: "hoyolab" },
  { id: "twhkmo", name: "TW HK MO", platform: "hoyolab" },
];

type Props = {
  onServerChosen?: (server: Server) => void;
  onCookieChosen?: () => void;
};

export default function ToLoginScreen(props: Props) {
  const navigation = useNavigation();

  const handleChoseServer = (server: Server) => {
    props.onServerChosen && props.onServerChosen(server);
    // @ts-ignore
    navigation.navigate(SCREENS.LoginPage.id, {
      serverId: server.id,
      platform: server.platform,
    });
  };

  return (
    <View style={{ gap: 12 }}>
      <Text className="text-[14px] font-[HY55] text-black leading-5">
        请选择账号所在服务器。
      </Text>
      {hsrServers.map((server) => (
        <TextButton
          onPress={() => {
            handleChoseServer(server);
          }}
          key={server.id}
          hasShadow={false}
          width={"100%"}
          height={46}
        >
          {server.name}
        </TextButton>
      ))}
      <TextButton
        onPress={() => {
          props.onCookieChosen && props.onCookieChosen();
        }}
        hasShadow={false}
        width={"100%"}
        height={46}
      >
        手动设置
      </TextButton>
    </View>
  );
}
