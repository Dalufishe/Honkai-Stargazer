import React, { useMemo, useState } from "react";
import FilterBtn from "./FilterBtn/FilterBtn";
import { View } from "react-native";
import FilterPopUp from "../../../global/FilterPopUp/FilterPopUp";
import useCharFilter from "../../../../redux/characterFilter/useCharFilter";
import Path from "../../../../constant/path";
import PathMap from "../../../../../assets/images/images_map/path";
import CombatTypeMap from "../../../../../assets/images/images_map/combatType";
import { Path as PathType } from "../../../../types/path";
import { CombatType as CombatTypeType } from "../../../../types/combatType";
import { ZH_CN } from "../../../../../locales/zh_cn";

export default function FilterAction() {
  const [open, setOpen] = useState(false);

  const { charFilter, charFilterSelected, setCharFilterSelected } =
    useCharFilter();

  const charFilterItems = useMemo(
    () =>
      charFilter?.map((item) => ({
        value: item.id,
        name: ZH_CN[item.id],
        icon: Path.includes(item.id)
          ? PathMap[item.id as PathType].icon
          : CombatTypeMap[item.id as CombatTypeType].icon,
      })),
    [charFilter]
  );

  return (
    <View>
      <FilterBtn
        onPress={() => {
          setOpen(!open);
        }}
      />
      {open && (
        <FilterPopUp
          items={charFilterItems!}
          value={charFilterSelected!}
          // @ts-ignore
          onChange={setCharFilterSelected}
          onClose={() => {
            setOpen(false);
          }}
          onReset={() => {
            setCharFilterSelected([]);
          }}
          onConfirm={() => {
            setOpen(false);
          }}
        />
      )}
    </View>
  );
}
