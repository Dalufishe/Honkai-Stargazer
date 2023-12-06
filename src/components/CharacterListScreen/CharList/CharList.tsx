import React, { useEffect, useMemo, useState } from "react";
import { ScrollView, View } from "react-native";
import CharCard from "../../global/CharCard/CharCard";
import { SCREENS } from "../../../constant/screens";
import { useNavigation } from "@react-navigation/native";
import characterList from "../../../../data/character_data/character_list.json";
import * as character_list_map from "../../../../data/character_data/@character_data_map/character_data_map";
import * as images_map from "../../../../assets/images/@images_map/images_map";
import { CharacterCard, CharacterName } from "../../../types/character";

/*
const testImage1 = require("../../../../assets/images/test-charlist-img-1.png");
const testImage2 = require("../../../../assets/images/test-charlist-img-2.png");
const testImage3 = require("../../../../assets/images/test-charlist-img-3.png");
const testImage4 = require("../../../../assets/images/test-charlist-img-4.png");
const testImage5 = require("../../../../assets/images/test-charlist-img-5.png");
const testImage6 = require("../../../../assets/images/test-charlist-img-6.png");


let DATA_SET = [
  { image: testImage1, star: 5, name: "镜流" },
  { image: testImage2, star: 4, name: "停云" },
  { image: testImage3, star: 5, name: "刃" },
  { image: testImage4, star: 5, name: "希儿" },
  { image: testImage5, star: 5, name: "符玄" },
  { image: testImage6, star: 5, name: "布洛妮娅" },
  // 可以添加更多的元素
];
*/

type Props = {
  reverse?: boolean;
};

export default function CharList(props: Props) {
  const navigation = useNavigation();

  // get characters' data
  const [charCardListData, setCharCardListData] = useState<CharacterCard[]>();
  useEffect(() => {
    setCharCardListData(
      characterList.map((char) => ({
        id: char.name,
        name:
          character_list_map.ZH_CN[char.name as CharacterName]?.name ||
          char.name,
        rare: char.rare,
        image: images_map.Chacracter[char.name as CharacterName]?.icon,
      }))
    );
  }, []);

  const charCardListJSX = useMemo(
    () =>
      charCardListData?.map((item, i) => (
        <CharCard
          key={i}
          onPress={() => {
            // @ts-ignore
            navigation.navigate(SCREENS.CharacterPage.id, {
              id: item?.id,
              name: item?.name,
            });
          }}
          {...item}
        />
      )),
    [charCardListData]
  );

  return (
    <View style={{ width: "100%" }} className="z-30">
      <ScrollView style={{ padding: 17, paddingBottom: 0 }}>
        <View
          style={{
            paddingVertical: 110,
            flexDirection: "row",
            flexWrap: "wrap",
            gap: 11,
            justifyContent: "center",
          }}
        >
          {props.reverse ? charCardListJSX?.slice().reverse() : charCardListJSX}
        </View>
      </ScrollView>
    </View>
  );
}
