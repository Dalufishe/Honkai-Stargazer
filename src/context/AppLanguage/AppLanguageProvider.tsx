import React from "react";
import { AppLanguage } from "../../types/language";
import useLocalState from "../../hooks/useLocalState";
import AppLanguageContext from "./AppLanguageContext";

export default function AppLanguageProvider({ children }: { children: any }) {
  const [language, setLanguage] = useLocalState<AppLanguage>(
    "app-language",
    "zh_cn"
  );

  return (
    <AppLanguageContext.Provider value={{ language, setLanguage }}>
      {children}
    </AppLanguageContext.Provider>
  );
}
