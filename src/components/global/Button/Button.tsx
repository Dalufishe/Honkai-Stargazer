import React from "react";
import {
  DimensionValue,
  StyleProp,
  Text,
  TouchableOpacity,
  View,
  ViewStyle,
} from "react-native";
import { GestureResponderEvent } from "react-native-modal";
import { Shadow } from "react-native-shadow-2";

export default function Button({
  children,
  width,
  height,
  onPress,
  hasShadow = true,
  disable = false,
  style,
}: {
  children: any;
  width: DimensionValue;
  height: DimensionValue;
  onPress?: (e: GestureResponderEvent) => void;
  hasShadow?: boolean;
  disable?: boolean;
  style?: StyleProp<ViewStyle>;
}) {
  return disable ? (
    hasShadow ? (
      <Shadow offset={[0, 4]}>
        <View
          className="bg-[#dddddd] rounded-[49px] p-[3px]"
          style={[
            {
              width,
              height,
              justifyContent: "center",
              alignItems: "center",
            },
            style,
          ]}
        >
          <View
            className="rounded-[49px] border border-solid border-[#c7c7c7] w-full h-full"
            style={{
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            {children}
          </View>
        </View>
      </Shadow>
    ) : (
      <View
        className="bg-[#dddddd] rounded-[49px] p-[3px]"
        style={[
          {
            width,
            height,
            justifyContent: "center",
            alignItems: "center",
          },
          style,
        ]}
      >
        <View
          className="rounded-[49px] border border-[#c7c7c7] w-full h-full"
          style={{
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          {children}
        </View>
      </View>
    )
  ) : (
    <TouchableOpacity activeOpacity={0.65} onPress={onPress}>
      {hasShadow ? (
        <Shadow offset={[0, 4]}>
          <View
            className="bg-[#dddddd] rounded-[49px] p-[3px]"
            style={[
              {
                width,
                height,
                justifyContent: "center",
                alignItems: "center",
              },
              style,
            ]}
          >
            <View
              className="rounded-[49px] border border-solid border-[#c7c7c7] w-full h-full"
              style={{
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              {children}
            </View>
          </View>
        </Shadow>
      ) : (
        <View
          className="bg-[#dddddd] rounded-[49px] p-[3px]"
          style={[
            {
              width,
              height,
              justifyContent: "center",
              alignItems: "center",
            },
            style,
          ]}
        >
          <View
            className="rounded-[49px] border border-[#c7c7c7] w-full h-full"
            style={{
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            {children}
          </View>
        </View>
      )}
    </TouchableOpacity>
  );
}
