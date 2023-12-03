import { ExpoImage } from "./image";

export type Lightcone = {
  id?: LightconeName;
  name?: string;
  image?: ExpoImage;
  imageFull?: ExpoImage;
  rare?: number;
  path?: string;
  description?: string;
};

export type LightconeCard = {
  id: string;
  name: string;
  rare: number;
  image: ExpoImage;
};

export type LightconeName =
  | "An Instant Before A Gaze"
  | "Night of Fright"
  | "Worrisome, Blissful"
  | "Brighter Than the Sun"
  | "I Shall Be My Own Sword"
  | "Time Waits for No One"
  | "She Already Shut Her Eyes"
  | "Before Dawn"
  | "The Unreachable Side"
  | "Echoes of the Coffin"
  | "Incessant Rain"
  | "Patience Is All You Need"
  | "Moment of Victory"
  | "In the Name of the World"
  | "But the Battle Isn't Over"
  | "Something Irreplaceable"
  | "In the Night"
  | "Night on the Milky Way"
  | "Solitary Healing"
  | "Texture of Memories"
  | "Cruising in the Stellar Sea"
  | "On the Fall of an Aeon"
  | "Hey, Over Here"
  | "Before the Tutorial Mission Starts"
  | "Today Is Another Peaceful Day"
  | "Nowhere to Run"
  | "Carve the Moon, Weave the Clouds"
  | "Return to Darkness"
  | "This Is Me!"
  | "We Will Meet Again"
  | "Warmth Shortens Cold Nights"
  | "The Seriousness of Breakfast"
  | "Woof! Walk Time!"
  | "Past and Future"
  | "River Flows in Spring"
  | "We Are Wildfire"
  | "Fermata"
  | "Quid Pro Quo"
  | "Geniuses' Repose"
  | "Under the Blue Sky"
  | "Dance! Dance! Dance!"
  | "Subscribe for More!"
  | "Trend of the Universal Market"
  | "Resolution Shines As Pearls of Sweat"
  | "Perfect Timing"
  | "Make the World Clamor"
  | "A Secret Vow"
  | "Planetary Rendezvous"
  | "Swordplay"
  | "Landau's Choice"
  | "Eyes of the Prey"
  | "Shared Feeling"
  | "The Birth of the Self"
  | "The Moles Welcome You"
  | "Memories of the Past"
  | "Only Silence Remains"
  | "Day One of My New Life"
  | "Good Night and Sleep Well"
  | "Post-Op Conversation"
  | "Sagacity"
  | "Mediation"
  | "Hidden Shadow"
  | "Pioneering"
  | "Mutual Demise"
  | "Multiplication"
  | "Adversarial"
  | "Passkey"
  | "Meshing Cogs"
  | "Loop"
  | "Defense"
  | "Shattered Home"
  | "Fine Fruit"
  | "Darting Arrow"
  | "Data Bank"
  | "Chorus"
  | "Void"
  | "Amber"
  | "Collapsing Sky"
  | "Cornucopia"
  | "Arrows";