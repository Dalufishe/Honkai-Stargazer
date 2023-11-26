import React, { useState } from "react";
import { TouchableOpacity, View } from "react-native";
import MoreBtn from "../../../global/ui/MoreBtn/MoreBtn";
import List from "../../../global/ui/List/List";
import ListItem from "../../../global/ui/List/ListItem";
import { useClickOutside } from "react-native-click-outside";

export default function PlayerAction() {
  const [isPress, setIsPress] = useState(false);

  const containerRef = useClickOutside<View>(() => {
    if (isPress) {
      setIsPress(false);
    }
  });

  return (
    <View className="z-50" ref={containerRef}>
      <MoreBtn
        onPress={() => {
          setIsPress(!isPress);
        }}
      />
      {isPress && (
        <View className="absolute right-0 top-8">
          <List>
            <TouchableOpacity activeOpacity={0.65}>
              <ListItem>账号管理</ListItem>
            </TouchableOpacity>
            <TouchableOpacity activeOpacity={0.65}>
              <ListItem>编辑首页</ListItem>
            </TouchableOpacity>
            <TouchableOpacity activeOpacity={0.65}>
              <ListItem>设置</ListItem>
            </TouchableOpacity>
          </List>
        </View>
      )}
    </View>
  );
}