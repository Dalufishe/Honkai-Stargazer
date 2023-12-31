import React from "react";
import SettingGroup from "../../SettingGroup/SettingGroup";
import SettingItem from "../../SettingGroup/SettingItem/SettingItem";
import Toast from "../../../../utils/toast/Toast";

export default function NotificationSetting() {
  return (
    <SettingGroup title="通知">
      <SettingItem
        type="navigation"
        title="所有通知"
        content="關"
        onNavigate={() => {
          Toast.StillDevelopingToast();
        }}
      />
      <SettingItem
        type="navigation"
        title="开拓力"
        content="關"
        onNavigate={() => {
          Toast.StillDevelopingToast();
        }}
      />
      <SettingItem
        type="navigation"
        title="派遣委托"
        content="關"
        onNavigate={() => {
          Toast.StillDevelopingToast();
        }}
      />
      <SettingItem
        type="navigation"
        title="每日实训"
        content="關"
        onNavigate={() => {
          Toast.StillDevelopingToast();
        }}
      />
      <SettingItem
        type="navigation"
        title="模拟宇宙"
        content="關"
        onNavigate={() => {
          Toast.StillDevelopingToast();
        }}
      />
    </SettingGroup>
  );
}
