import React, { useMemo, useState } from "react";
import FilterBtn from "./FilterBtn/FilterBtn";
import { View } from "react-native";
import FilterPopUp from "../../../global/FilterPopUp/FilterPopUp";
import Path from "../../../../constant/path";
import PathMap from "../../../../../assets/images/images_map/path";
import useLcFilter from "../../../../redux/lightconeFilter/useLcFilter";
import CombatTypeMap from "../../../../../assets/images/images_map/combatType";
import { Path as PathType } from "../../../../types/path";
import { CombatType as CombatTypeType } from "../../../../types/combatType";
import { ZH_CN } from "../../../../../locales/zh_cn";

export default function FilterAction() {
  const [open, setOpen] = useState(false);

  const { lcFilter, lcFilterSelected, setLcFilterSelected } = useLcFilter();

  const lcFilterItems = useMemo(
    () =>
      lcFilter?.map((item) => ({
        value: item.id,
        name: ZH_CN[item.id],
        icon: Path.includes(item.id)
          ? PathMap[item.id as PathType].icon
          : CombatTypeMap[item.id as CombatTypeType].icon,
      })),
    [lcFilter]
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
          items={lcFilterItems!}
          value={lcFilterSelected!}
          // @ts-ignore
          onChange={setLcFilterSelected}
          onClose={() => {
            setOpen(false);
          }}
          onReset={() => {
            setLcFilterSelected([]);
          }}
          onConfirm={() => {
            setOpen(false);
          }}
        />
      )}
    </View>
  );
}
