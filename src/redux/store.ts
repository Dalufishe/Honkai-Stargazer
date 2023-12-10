// redux

import { createStore, applyMiddleware, combineReducers } from "redux";
import { persistStore, persistReducer } from "redux-persist";
import AsyncStorage from "@react-native-async-storage/async-storage";

// reducers

import { hsrServerChosen } from "./hsrServerChosen/hsrServerChosen.reducer";
import { hoyolabCookie } from "./hoyolabCookie/hoyolabCookie.reducer";

const reducer = combineReducers({ hsrServerChosen, hoyolabCookie });

// persist
const persistConfig = {
  key: "root",
  storage: AsyncStorage,
  whitelist: ["hsrServerChosen", "hoyolabCookie"],
};

// @ts-ignore
const persistedReducer = persistReducer(persistConfig, reducer);

// middlewares
const middlewares: any[] = [];

// store
const store = createStore(persistedReducer, applyMiddleware(...middlewares));

export type RootState = ReturnType<typeof store.getState>;

const persistor = persistStore(store);

export { store, persistor };