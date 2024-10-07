package fi.altanar.batmob.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.Spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.awt.font.TextAttribute;
import java.awt.Color;

public class SpellTriggers {

    List<Pattern> patterns;

    LinkedList spells = new LinkedList();

    Map<String, Object[]> colors = new HashMap<String, Object[]>();
    Map<String, Object[]> damageTypeColors = new HashMap<String, Object[]>();

    public SpellTriggers() {
        // this.targetPattern = Pattern.compile(PATTERN_TARGET);

        this.colors.put("heal", new Object[] { TextAttribute.FOREGROUND, new Color(0x008000) });
        this.colors.put("partyheal", new Object[] { TextAttribute.FOREGROUND, new Color(0x00FF00) });
        this.colors.put("damage", new Object[] { TextAttribute.FOREGROUND, new Color(0xFFFF00) });
        this.colors.put("areadamage", new Object[] { TextAttribute.FOREGROUND, new Color(0xFFFF00) });
        this.colors.put("utility", new Object[] { TextAttribute.FOREGROUND, new Color(0xFFFFFF) });
        this.colors.put("teleport", new Object[] { TextAttribute.FOREGROUND, new Color(0x800080) });
        this.colors.put("boost", new Object[] { TextAttribute.FOREGROUND, new Color(0x00FF00) });
        this.colors.put("prot", new Object[] { TextAttribute.FOREGROUND, new Color(0x0000FF) });
        this.colors.put("harm", new Object[] { TextAttribute.FOREGROUND, new Color(0x800000) });
        this.colors.put("field", new Object[] { TextAttribute.FOREGROUND, new Color(0x00FFFF) });
        this.colors.put("stun", new Object[] { TextAttribute.FOREGROUND, new Color(0x0000FF) });
        this.colors.put("dest", new Object[] { TextAttribute.FOREGROUND, new Color(0xFFFFFF), TextAttribute.BACKGROUND,
                new Color(0x800080) });

        this.damageTypeColors.put("phys", new Object[] { TextAttribute.FOREGROUND, new Color(0x808000) });
        this.damageTypeColors.put("fire", new Object[] { TextAttribute.FOREGROUND, new Color(0xFF0000) });
        this.damageTypeColors.put("cold", new Object[] { TextAttribute.FOREGROUND, new Color(0x00FFFF) });
        this.damageTypeColors.put("elec", new Object[] { TextAttribute.FOREGROUND, new Color(0x0000FF) });
        this.damageTypeColors.put("acid", new Object[] { TextAttribute.FOREGROUND, new Color(0x008000) });
        this.damageTypeColors.put("poison", new Object[] { TextAttribute.FOREGROUND, new Color(0x00FF00) });
        this.damageTypeColors.put("asphyx", new Object[] { TextAttribute.FOREGROUND, new Color(0xFF00FF) });
        this.damageTypeColors.put("magic", new Object[] { TextAttribute.FOREGROUND, new Color(0xFFFF00) });
        this.damageTypeColors.put("psi", new Object[] { TextAttribute.FOREGROUND, new Color(0x4080FF) });
        this.damageTypeColors.put("harm", new Object[] { TextAttribute.FOREGROUND, new Color(0xC0C000) });

        this.define("heal self", "heal", "none", "'judicandus littleee'");
        this.define("cure light wounds", "heal", "none", "'judicandus mercuree'");
        this.define("cure serious wounds", "heal", "none", "'judicandus ignius'");
        this.define("cure critical wounds", "heal", "none", "'judicandus mangenic'");
        this.define("minor heal", "heal", "none", "'judicandus pzarcumus'");
        this.define("major heal", "heal", "none", "'judicandus pafzarmus'");
        this.define("true heal", "heal", "none", "'judicandus zapracus'");
        this.define("half heal", "heal", "none", "'pzzzar paf'");
        this.define("heal", "heal", "none", "'pzzzarr'");
        this.define("deaths door", "heal", "none", "'mumbo jumbo'");
        this.define("runic heal", "heal", "none", "'!\\* \\*'");
        this.define("heal body", "heal", "none", "'ZAP ZAP ZAP!'");

        this.define("remove poison", "heal", "none", "'judicandus saugaiii'");
        this.define("cure player", "heal", "none", "'freudemas egoid'");
        this.define("restore", "heal", "none", "'Siwa on selvaa saastoa.'");
        this.define("natural renewal", "heal", "none", "'Naturallis Judicandus Imellys'");

        this.define("raise dead", "heal", "none", "'vokinsalak elfirtluassa'");
        this.define("resurrection", "heal", "none", "'tuo fo wen stanhc'");
        this.define("new body", "heal", "none", "'corpus novus foobar'");
        this.define("reincarnation", "heal", "none", "'henget uusix'");
        this.define("reanimation", "heal", "none", "'Blaarh ARGHAGHAHAHA URAAAH BELARGH!'");

        this.define("venturers way", "heal", "none", "'.a few steps to earthen might, a few steps.*");
        this.define("shattered feast", "heal", "none", "'That I have set free, return to me'");
        this.define("laying on hands", "heal", "none", "'Renew our strength'");

        // Party heal spells

        this.define("minor party heal", "partyheal", "none", "'judicandus puorgo ignius'");
        this.define("major party heal", "partyheal", "none", "'judicandus puorgo mangenic'");
        this.define("true party heal", "partyheal", "none", "'judicandus eurto mangenic'");
        this.define("heal all", "partyheal", "none", "'koko mudi kuntoon, hep'");
        this.define("blessed warmth", "partyheal", "none", "'! \\(\\*\\) !'");
        this.define("campfire tune", "partyheal", "none", "'What child is this, who laid to rest on Mary's.*");

        // Utility spells

        this.define("paranoia", "utility", "none", "'noxim delusa'");
        this.define("identify relic", "utility", "none", "'srR' Upon\\^nep'");
        this.define("good berry", "utility", "none", "'sezdaron montir'");
        this.define("mirror image", "utility", "none", "'peilikuvia ja lasinsirpaleita'");
        this.define("floating disc", "utility", "none", "'rex car bus xzar'");
        this.define("light", "utility", "none", "'ful'");
        this.define("greater light", "utility", "none", "'vas ful'");
        this.define("darkness", "utility", "none", "'na ful'");
        this.define("greater darkness", "utility", "none", "'vas na ful'");
        this.define("moon sense", "utility", "none", "'daaa timaaa of daaa maaanth'");
        this.define("see invisible", "utility", "none", "'\\$%&@ #\\*%@\\*@# \\$&\\*@#'");
        this.define("see magic", "utility", "none", "'ahne paskianen olen ma kun taikuutta nahda tahdon'");
        this.define("floating", "utility", "none", "'rise Rise RISE'");
        this.define("water walking", "utility", "none", "'Jeeeeeeeeeeeesuuuuuuuus'");
        this.define("replenish ally", "utility", "none", "'enfuego delyo'");
        this.define("drain ally", "utility", "none", "'enfuego delmigo'");
        this.define("enhance vision", "utility", "none", "'isar avatap patyan'");
        this.define("invisibility", "utility", "none", "'\\.\\.\\.\\.\\. \\.\\.\\.\\. \\.\\.\\. \\.\\.  \\.    \\.'");
        this.define("aura detection", "utility", "none", "'fooohh haaahhh booooloooooh'");
        this.define("feather weight", "utility", "none", "'transformaticus minimus'");
        this.define("floating letters", "utility", "none", "'lentavia lauseita'");
        this.define("wizard eye", "utility", "none", "'mad rad dar'");
        this.define("all-seeing eye", "utility", "none", "'tamakan natelo assim'");
        this.define("levitation", "utility", "none", "'etati elem ekam'");
        this.define("there not there", "utility", "none", "'jakki makupala'");
        this.define("mental watch", "utility", "none", "'kakakaaa  tsooon'");
        this.define("mental glance", "utility", "none", "'vaxtextraktdryck'");
        this.define("spellteaching", "utility", "none", "'moon fiksu, soot tyhma  - opi tasta taika'");
        this.define("word of recall", "utility", "none", "'vole love velo levo'");
        this.define("call pigeon", "utility", "none", "'habbi urblk'");
        this.define("summon blade", "utility", "none", "'ahieppa weaapytama nyttemmin'");
        this.define("identify", "utility", "none", "'mega visa huijari'");
        this.define("remove scar", "utility", "none", "'lkzaz zueei enz orn'");
        this.define("infravision", "utility", "none", "'demoni on pomoni'");
        this.define("drain room", "utility", "none", "'enfuegome delterra'");
        this.define("drain item", "utility", "none", "'enfuego delcosa'");
        this.define("detect misery", "utility", "none", "'misery ior noctar report'");
        this.define("see the light", "utility", "none", "'ogyawaelbirroh'");
        this.define("satiate person", "utility", "none", "'Creo Herbamus Satisfus'");
        this.define("detect alignment", "utility", "none", "'annihilar hzzz golum'");
        this.define("create money", "utility", "none", "'roope ankka rulettaa'");
        this.define("create food", "utility", "none", "'juustoa ja pullaa, sita mun maha halajaa'");
        this.define("transmute self", "utility", "none", "'nihenuak assaam no nek orrek'");
        this.define("life link", "utility", "none", "'Corporem Connecticut Corporee'");
        this.define("drain pool", "utility", "none", "fiery golden runes in mid-air '\\$ !\\^'");
        this.define("detect poison", "utility", "none", "fiery blue sigla '\\$ !\\^'");
        this.define("youth", "utility", "none", "'Akronym Htouy, Hokrune Arafax'");
        this.define("replenish energy", "utility", "none", "'!\\* %'");
        this.define("clairvoyance", "utility", "none", "'aalltyyuii regonza zirii'");
        this.define("damn armament", "utility", "none", "'Gawd DAMN IT!'");
        this.define("sex change", "utility", "none", "'likz az zurgeeon'");
        this.define("charge staff", "utility", "none", "'# !\\(");
        this.define("shapechange", "utility", "none", "'!\\('");
        this.define("chaotic warp", "utility", "none", "'weaapytama wezup boomie'");
        this.define("transform golem", "utility", "none", "'insignificus et gargantum alternos'");
        this.define("remote banking", "utility", "none", "'bat-o-mat'");
        this.define("dragonify", "utility", "none", "'mun enoni oli rakoni'");
        this.define("tiger mask", "utility", "none", "'Tiger Power!'");
        this.define("detect race", "utility", "none", "'taxonimus zoologica whaddahellizzat'");
        this.define("kings feast", "utility", "none", "'If you look behind a spider web, when it is covered with.*");
        this.define("jesters trivia", "utility", "none", "'Green skins, white skins, black skins, purple skins.*");
        this.define("soothing sounds", "utility", "none", "'Now that two decades gone by and I know that's a long.*");
        this.define("achromatic eyes", "utility", "none", "'Stand confused with lack of comprehension, pagan of.*");
        this.define("vigilant melody", "utility", "none", "'Lost I am not but knowledge I seek for there, my friend.*");
        this.define("catchy singalong", "utility", "none", "'Shooting Star'");
        this.define("spider servant", "utility", "none", "'infernalicus conjuratis arachnidos'");
        this.define("spider walk", "utility", "none", "'Khizanth Arachnidus Walkitus'");
        this.define("blade of fire", "utility", "none", "'dsaflk aslfoir'");
        this.define("transfer mana", "utility", "none", "'\"\\) !#'");
        this.define("venom blade", "utility", "none", "'May this become the blood of the Spider queen'");
        this.define("inquiry to ahm", "utility", "none", "'!\\?'");
        this.define("bless armament", "utility", "none", "'Faerwon, grant your favor!'");
        this.define("sweet lullaby", "utility", "none", "'There is nothing you can do, when you realize with.*");
        this.define("bless vial", "utility", "none", "'Zanctum Zanctus Aqua'");
        this.define("patch item", "utility", "none", "'jimmiii fixiiii'");
        this.define("cantrip", "utility", "none", "'Vita non est vivere sed valere vita est'");
        this.define("musicians alm", "utility", "none", "'Donations welcome'");
        this.define("singing shepherd", "utility", "none",
                "'Squirrel in the dirt, squirrel in the pool, squirrel don't get hurt, trying to stay cool!'");
        this.define("arms lore", "utility", "none", "'well, what have we here'");
        this.define("monster lore", "utility", "none", "'haven't I seen you before\\?'");
        this.define("lift of load", "utility", "none", "'Myh myh!'");
        this.define("create herb", "utility", "none", "'greeeenie fiiingerie'");
        this.define("money is power", "utility", "none", " \\${24}'");
        this.define("preserve corpse", "utility", "none", "'upo huurre helkama'");

        this.define("create air armour", "utility", "none", "'bloozdy etherum errazam zunk'");
        this.define("sounds of silence", "utility", "none", "'Hear this charm, there in the dark, lurking fiends.*");
        this.define("wilderness location", "utility", "none", "'spirits of nature, show me the way!'");
        this.define("clandestine thoughts", "utility", "none",
                "'To all the eyes around me, I wish to remain hidden, must I.*");
        this.define("natural transformation", "utility", "none", "'@& \\^'");
        this.define("protect weapon/armour/item", "utility", "none", "'blueeeeeeeeeee\\*\\*\\*\\*saka\\?\\?am!a'");
        this.define("uncontrollable hideous laughter", "utility", "none", "'nyuk nyuk nyuk'");

        this.define("spider demon conjuration", "utility", "none", "'arachnid infernalicus arachnoidus demonicus'");
        this.define("spider demon channeling", "utility", "none", "'infernalicus nexus arachnid rex'");
        this.define("spider demon control", "utility", "none", "'infernalicus domus arachnid rex'");
        this.define("prayer to the spider queen", "utility", "none", "'Khizanth Arachnidus Satisfusmus'");
        this.define("spider demon mass sacrifice", "utility", "none", "'infernalicus domus arachnid rex magnos'");
        this.define("spider demon banishment", "utility", "none", "'infernalicus thanatos arachnidos'");
        this.define("spider demon inquiry", "utility", "none", "'Khirsah Zokant Arachnidus'");

        // Fields

        this.define("force dome", "field", "none", "'xulu tango charlie'");
        this.define("imprisonment", "field", "none", "'imprickening zang gah'");
        this.define("field of fear", "field", "none", "'wheeeaaaaaa oooooo'");
        this.define("anti magic field", "field", "none", "'taikoja ma inhoan'");
        this.define("electric field", "field", "elec", "'Ziiiiiiiiit Ziiit Ziiiit'");
        this.define("shelter", "field", "none", "'withing thang walz'");
        this.define("neutralize field", "field", "none", "'null, nill, noll, nutin'");
        this.define("rain", "field", "none", "'huku mopo huku'");
        this.define("drying wind", "field", "none", "'hooooooooooowwwwwwwwwwwlllllllllllllll'");
        this.define("create mud", "field", "none", "'# !#'");
        this.define("field of light", "field", "none", "'ja nyt kenka kepposasti nousee'");
        this.define("celestial haven", "field", "none", "'zeriqum'");

        // Offensive spells

        this.define("magic missile", "damage", "magic", "'gtzt zur fehh'");
        this.define("summon lesser spores", "damage", "magic", "'gtzt zur sanc'");
        this.define("levin bolt", "damage", "magic", "'gtzt zur semen'");
        this.define("summon greater spores", "damage", "magic", "'gtzt mar nak semen'");
        this.define("golden arrow", "damage", "magic", "'gtzt mar nak grttzt'");

        this.define("shocking grasp", "damage", "elec", "'zot zur fehh'");
        this.define("lightning bolt", "damage", "elec", "'zot zur sanc'");
        this.define("blast lightning", "damage", "elec", "'zot zur semen'");
        this.define("forked lightning", "damage", "elec", "'zot mar nak semen'");
        this.define("electrocution", "damage", "elec", "'zot mar nak grttzt'");

        this.define("disruption", "damage", "acid", "'fzz zur fehh'");
        this.define("acid wind", "damage", "acid", "'fzz zur sanc'");
        this.define("acid arrow", "damage", "acid", "'fzz zur semen'");
        this.define("acid ray", "damage", "acid", "'fzz mar nak semen'");
        this.define("acid blast", "damage", "acid", "'fzz mar nak grttzt'");

        this.define("flame arrow", "damage", "fire", "'fah zur fehh'");
        this.define("firebolt", "damage", "fire", "'fah zur sanc'");
        this.define("fire blast", "damage", "fire", "'fah zur semen'");
        this.define("meteor blast", "damage", "fire", "'fah mar nak semen'");
        this.define("lava blast", "damage", "fire", "'fah mar nak grttzt'");

        this.define("thorn spray", "damage", "poison", "'krkx zur fehh'");
        this.define("poison blast", "damage", "poison", "'krkx zur sanc'");
        this.define("venom strike", "damage", "poison", "'krkx zur semen'");
        this.define("power blast", "damage", "poison", "'krkx mar nak semen'");
        this.define("summon carnal spores", "damage", "poison", "'krkx mar nak grttzt'");

        this.define("vacuumbolt", "damage", "asphyx", "'ghht zur fehh'");
        this.define("suffocation", "damage", "asphyx", "'ghht zur sanc'");
        this.define("chaos bolt", "damage", "asphyx", "'ghht zur semen'");
        this.define("strangulation", "damage", "asphyx", "'ghht mar nak semen'");
        this.define("blast vacuum", "damage", "asphyx", "'ghht mar nak grttzt'");

        this.define("mind blast", "damage", "psi", "'omm zur fehh'");
        this.define("psibolt", "damage", "psi", "'omm zur sanc'");
        this.define("psi blast", "damage", "psi", "'omm zur semen'");
        this.define("mind disruption", "damage", "psi", "'omm mar nak semen'");
        this.define("psychic crush", "damage", "psi", "'tora tora tora'");

        this.define("chill touch", "damage", "cold", "'cah zur fehh'");
        this.define("flaming ice", "damage", "cold", "'cah zur sanc'");
        this.define("darkfire", "damage", "cold", "'cah zur semen'");
        this.define("icebolt", "damage", "cold", "'cah mar nak semen'");
        this.define("cold ray", "damage", "cold", "'cah mar nak grttzt'");

        this.define("con fioco", "damage", "fire", "'AeaH\\*h\\*\\*\\*Gdg'");
        this.define("noituloves dischord", "damage", "phys", "'dIsCHoRD'");
        this.define("dancing blades", "damage", "phys", "'Dance my little blades, whip my enemies for I am.*");

        this.define("channelbolt", "damage", "elec", "'tsaibaa'");
        this.define("channelball", "damage", "magic", "'shar ryo den\\.\\.\\.Haa!'");
        this.define("channelburn", "damage", "fire", "'grhagrhagrhagrah gra gra Hyaa!'");
        this.define("channelray", "damage", "magic", "'lecaps meeb nonnock'");
        this.define("channelspray", "damage", "fire", "'grinurb sdan imflagrum'");
        this.define("drain enemy", "damage", "none", "'enfuego delvivendo'");

        this.define("cause light wounds", "damage", "harm", "'tosi pieni neula'");
        this.define("cause serious wounds", "damage", "harm", "'rhuuuumm angotz amprltz'");
        this.define("cause critical wounds", "damage", "harm", "'rhuuuumm angotz klekltz'");
        this.define("hemorrhage", "damage", "harm", "'yugzhrr'");
        this.define("aneurysm", "damage", "harm", "'yugzhrr paf'");
        this.define("harm body", "damage", "harm", "'PAF PAF PAF!'");
        this.define("half harm", "damage", "harm", "'ruotsalainen ensiapu'");
        this.define("harm", "damage", "harm", "'puujalka jumalauta'");

        this.define("word of attrition", "damage", "magic", "'khozak'");
        this.define("word of destruction", "damage", "magic", "'Sherpha!'");
        this.define("word of blasting", "damage", "magic", "'hraugh'");
        this.define("word of genocide", "damage", "magic", "'dephtua'");
        this.define("word of slaughter", "damage", "magic", "'niinr'");
        this.define("word of spite", "damage", "magic", "'torrfra'");
        this.define("word of oblivion", "damage", "magic", "'FRONOX!!'");
        this.define("black hole", "damage", "asphyx", "'Azzarakk, take this sacrifice I offer thee!'");

        this.define("gem fire", "damage", "fire", "gem '& \\^'");
        this.define("hoar frost", "damage", "cold", "ice crystal '& \\^'");
        this.define("star light", "damage", "magic", "'!\\( !!'");
        this.define("wither flesh", "damage", "magic", "'\"# \\^'");

        this.define("dispel evil", "damage", "magic", "'Ez' div'");
        this.define("dispel good", "damage", "magic", "'whoosy banzziii pal eeeiizz dooneb'");
        this.define("dispel undead", "damage", "magic", "'Sanctum disqum'");
        this.define("holy bolt", "damage", "magic", "'Sanctum circum'");
        this.define("holy hand", "damage", "magic", "'Sanctus inxze'");
        this.define("saintly touch", "damage", "magic", "'Exzorde' �'");
        this.define("banish demons", "damage", "magic", "'Satan down'");
        this.define("wrath of las", "damage", "fire", "' Lassum  '");

        this.define("spider wrath", "damage", "poison", "'Khizanth Arachnidus Iracundus'");
        this.define("hunger of the spider", "damage", "poison", "'Khizanth Arachnidus Vitalis'");

        // Area spells

        this.define("meteor swarm", "areadamage", "fire", "'fah zur semen gnatlnamauch'");
        this.define("magic wave", "areadamage", "magic", "'gtzt zur semen gnatlnamauch'");
        this.define("vacuum ball", "areadamage", "asphyx", "'ghht zur semen gnatlnamauch'");
        this.define("cone of cold", "areadamage", "cold", "'cah zur semen gnatlnamauch'");
        this.define("chain lightning", "areadamage", "elec", "'zot zur semen gnatlnamauch'");
        this.define("acid rain", "areadamage", "acid", "'fzz zur semen gnatlnamauch'");
        this.define("poison spray", "areadamage", "poison", "'krkx zur semen gnatlnamauch'");
        this.define("psychic shout", "areadamage", "psi", "'omm zur semen gnatlnamauch'");

        this.define("fireball", "areadamage", "fire", "'zing yulygul bugh'");

        this.define("lava storm", "areadamage", "fire", "'fah mar nak grttzt gnatlnamauch'");
        this.define("magic eruption", "areadamage", "magic", "'gtzt mar nak grttzt gnatlnamauch'");
        this.define("vacuum globe", "areadamage", "asphyx", "'ghht mar nak grttzt gnatlnamauch'");
        this.define("hailstorm", "areadamage", "cold", "'cah mar nak grttzt gnatlnamauch'");
        this.define("lightning storm", "areadamage", "elec", "'zot mar nak grttzt gnatlnamauch'");
        this.define("acid storm", "areadamage", "acid", "'fzz mar nak grttzt gnatlnamauch'");
        this.define("killing cloud", "areadamage", "poison", "'krkx mar nak grttzt gnatlnamauch'");
        this.define("psychic storm", "areadamage", "psi", "'omm mar nak grttzt gnatlnamauch'");

        this.define("summon storm", "areadamage", "cold", "\\*\\* /\\|/");
        this.define("earthquake", "areadamage", "magic", "'%'");
        this.define("holy wind", "areadamage", "magic", "'Rev 'liz'");
        this.define("noituloves deathlore", "areadamage", "magic",
                "'Thar! Rauko! Mor! Ris-Rim! Fuin-Heru! GOR! Gurthgwath!n'");
        this.define("flames of righteousness", "areadamage", "magic", "'ex'domus naz'");

        // Teleportation spells

        this.define("summon", "teleport", "none", "'gwwaaajj'");
        this.define("teleport without error", "teleport", "none", "'xafe ayz xckgandhuzqarr'");
        this.define("teleport with error", "teleport", "none", "'xafe xyyqh xckgandhuzqarr'");
        this.define("relocate", "teleport", "none", "'xafe uurthq'");
        this.define("go", "teleport", "none", "'flzeeeziiiiying nyyyaaa'");
        this.define("mobile cannon", "teleport", "none", "'buuuummbzdiiiiiibummm'");
        this.define("dimension door", "teleport", "none", "'prtolala offf pwerrrr'");
        this.define("banish", "teleport", "none", "'havia kauhistus pois'");
        this.define("party banish", "teleport", "none", "'etsi poika pippuria'");
        this.define("mind store", "teleport", "none", "'memono locati'");
        this.define("pathfinder", "teleport", "none", "'Fo fu fe fum, Lord of the Winds, I know Thy.*");
        this.define("holy way", "teleport", "none", "'Avee Alee adudaaa..'");
        this.define("goto ship", "teleport", "none", "'etheria aquariq [a-z]+'");

        this.define("escape velocity", "teleport", "none", "'fzeziiignya'");
        this.define("slingshot vortex", "teleport", "none", "'aiwuhfiueh'");
        this.define("dimensional vacuum", "teleport", "none", "'uoogachucka'");
        this.define("planar gate", "teleport", "none", "'pqwioeu aslkdjfh zxmcb'");

        // Boost spells

        this.define("arches favour", "boost", "none", "'In the Shadows cast down by the moon, a.*");
        this.define("melodical embracement", "boost", "none",
                "'Once there were two knights and maidens They'd walk together.*");
        this.define("war ensemble", "boost", "none", "'War is TOTAL massacre, sport the war, war SUPPOORT!!!'");
        this.define("psionic shield", "boost", "none", "'niihek atierapip aj niiramaan aaffaj'");
        this.define("unpain", "boost", "none", "'harnaxan temnahecne'");
        this.define("blessing of tarmalen", "boost", "none", "'nilaehz arzocupne'");
        this.define("mind development", "boost", "none", "'Annatheer graaweizta'");
        this.define("unstable mutation", "boost", "none", "'ragus on etsat mumixam!'");
        this.define("energy aura yellow", "boost", "none", "'hhhnnnnnrrrrraaahhh!!'");
        this.define("energy aura red", "boost", "none", "'hnnn\\.\\.\\.\\.Urrgggg\\.\\.\\.\\.\\.RRAAHH!!!'");
        this.define("energy aura blue", "boost", "none", "'RRRRAAAAAHHRRRRGGGGGGHHH!!!!!'");
        this.define("earth blood", "boost", "none", "'!\\( \\*\\)");
        this.define("earth power", "boost", "none", "'% !\\^'");
        this.define("regeneration", "boost", "none", "'nilaehz temnahecne'");
        this.define("haste", "boost", "none", "'sakenoivasta voimasta'");
        this.define("aura of hate", "boost", "none", "'Feel your anger and strike with.*");

        this.define("artificial intelligence", "boost", "none", "'nitin mof'");
        this.define("aura of power", "boost", "none", "'noccon mof'");
        this.define("awareness", "boost", "none", "'siwwis mof'");
        this.define("giant strength", "boost", "none", "'rtsstr mof'");
        this.define("flame fists", "boost", "none", "'Polo Polomii'");
        this.define("glory of destruction", "boost", "none", "'Grant me the power, the fire from within'");
        this.define("blessing of intoxication", "boost", "none", "'I'm drunbk and I am all powerfuly!'");

        // Prot spells

        this.define("resist temptation", "prot", "none", "'qxx'rzzzz'");
        this.define("resist disintegrate", "prot", "none", "'bii thee dzname uv tii blaaaz drazon'");
        this.define("vine mantle", "prot", "none", "'\"\" !#");
        this.define("strength in unity", "prot", "none", "'You say you don't believe this unity will last,.*");
        this.define("protection from aging", "prot", "none", "'Tempora Rolex Timex'");
        this.define("unstun", "prot", "none", "'Paxus'");
        this.define("lessen poison", "prot", "none", "'Impuqueto es Bien'");
        this.define("protection from evil", "prot", "none", "'sanctus Exzordus'");
        this.define("protection from good", "prot", "none", "'Good is dumb'");
        this.define("flex shield", "prot", "none", "'\\^ !\\)");
        this.define("force shield", "prot", "none", "'thoiiiiiisss huuuiahashn'");
        this.define("personal force field", "prot", "none", "'riljya'");
        this.define("earth skin", "prot", "none", "'% !\\('");
        this.define("soul hold", "prot", "none", "'naxanhar hecnatemne'");
        this.define("guardian angel", "prot", "none", "'Judicandee iocus merciaa Tarmalen'");
        this.define("shield of faith", "prot", "none", "'Grant your worshipper your protection'");
        this.define("soul shield", "prot", "none", "'sanctus angeliq'");
        this.define("enhanced vitality", "prot", "none", "'zoot zoot zoot'");
        this.define("resist dispel", "prot", "none", "'zicks laai qluu'");
        this.define("iron will", "prot", "none", "'nostaaaanndiz noszum'");
        this.define("shield of protection", "prot", "none", "'nsiiznau'");
        this.define("blurred image", "prot", "none", "'ziiiuuuuns wiz'");
        this.define("displacement", "prot", "none", "'diiiiuuunz aaanziz'");
        this.define("force absorption", "prot", "phys", "'ztonez des deckers'");
        this.define("toxic dilution", "prot", "poison", "'morri nam pantoloosa'");
        this.define("heat reduction", "prot", "fire", "'hot hot not zeis daimons'");
        this.define("magic dispersion", "prot", "magic", "'meke tul magic'");
        this.define("energy channeling", "prot", "elec", "'kablaaaammmmm bliitz zundfer'");
        this.define("corrosion shield", "prot", "acid", "'sulphiraidzik hydrochloodriz gidz zuf'");
        this.define("ether boundary", "prot", "asphyx", "'qor monoliftus'");
        this.define("frost insulation", "prot", "cold", "'skaki barictos yetz fiil'");
        this.define("psychic sanctuary", "prot", "psi", "'toughen da mind reeez un biis'");
        this.define("armour of aether", "prot", "phys", "'fooharribah inaminos cantor'");
        this.define("shield of detoxification", "prot", "poison", "'nyiaha llaimay exchekes ployp'");
        this.define("flame shield", "prot", "fire", "'huppa huppa tiki tiki'");
        this.define("repulsor aura", "prot", "magic", "'shamarubu incixtes delfo'");
        this.define("lightning shield", "prot", "elec", "'ohm'");
        this.define("acid shield", "prot", "acid", "'hfizz hfizz nglurglptz'");
        this.define("aura of wind", "prot", "asphyx", "'englobo globo mc'pop'");
        this.define("frost shield", "prot", "cold", "'nbarrimon zfettix roi'");
        this.define("psionic phalanx", "prot", "psi", "'all for one, gather around me'");
        this.define("heavy weight", "prot", "none", "'tonnikalaa'");
        this.define("resist entropy", "prot", "none", "'Ourglazz Schmourglazz'");
        this.define("last rites", "prot", "none", "'Ab sinestris, mortum demitteri'");
        this.define("heavenly protection", "prot", "none", "'sanctus . o O'");
        this.define("quicksilver", "prot", "none", "'jumpiiz laika wabbitzz'");
        this.define("blessing of faerwon", "prot", "none", "'Benedic, Faerwon, nos et haec tua dona.*");
        this.define("reflector shield", "prot", "none", "'sakat ikkiak satsjaieh'");
        this.define("mana shield", "prot", "none", "'nullum driiiks umbah mana'");
        this.define("guardian", "prot", "none", "'  Zanctus -�- '");
        this.define("shadow armour", "prot", "none", "'klainmox'");
        this.define("stoneskin", "prot", "none", "'aflitruz'");
        this.define("resist gaseous form", "prot", "none", "'Break like the wind'");

        // Harming spells

        this.define("degenerate person", "harm", "none", "'kewa dan dol rae hout'");
        this.define("poison", "harm", "none", "'saugaiii'");
        this.define("forget", "harm", "none", "'sulta taiat pois, mulle hyva mieli taikadaikaduu'");
        this.define("make scar", "harm", "none", "'viiltaja jaska'");
        this.define("entropy", "harm", "none", "'vaka vanha vainamoinen'");
        this.define("area entropy", "harm", "none", "'vaka tosi vanha vainamoinen'");
        this.define("psychic purge", "harm", "none", "'aamad ato naav aanarub atyak ala'");
        this.define("terror", "harm", "none", "'BBBBOOOOOO!!!!'");
        this.define("mana drain", "harm", "none", "'I HATE MAGIC'");
        this.define("flip", "harm", "none", "'jammpa humppa ryydy mopsi'");
        this.define("hallucination", "harm", "none", "'huumeet miehen tiella pitaa'");
        this.define("curse of ogre", "harm", "none", "'rtsstr uurthg'");
        this.define("disease", "harm", "none", "'noccon uurthg'");
        this.define("feeblemind", "harm", "none", "'nitin uurthg'");
        this.define("amnesia", "harm", "none", "'siwwis uurthg'");
        this.define("wither", "harm", "none", "'xeddex uurthg'");
        this.define("life leech", "harm", "none", "'gimme urhits'");
        this.define("energy drain", "harm", "none", "'yugfzhrrr suuck suuuuuck suuuuuuuuuuck'");
        this.define("curse", "harm", "none", "'oli isa-sammakko, aiti-sammakko ja PIKKU-SAMMAKKO!!'");
        this.define("mellon collie", "harm", "none", "'Zmasching Pupkins's infanitsadnnes'");
        this.define("pestilence", "harm", "none", "'Harken to me and hear my plea, disease is what I call to thee'");
        this.define("curse of tarmalen", "harm", "none", "'nilaehz temnahecne neg'");
        this.define("suppress magic", "harm", "none", "'voi hellapoliisin kevatnuija'");
        this.define("energy vortex", "harm", "none", "'incantar enfeugo aggriva'");
        this.define("spider touch", "harm", "none", "'Khizanth Arachnidus Diametricus'");
        this.define("cleanse heathen", "harm", "none", "'Ala itke� iletys, parkua paha kuvatus'");
        this.define("permanent skill drain", "harm", "none", "'nyyjoo happa hilleiksis'");
        this.define("permanent spell drain", "harm", "none", "'nyyjoo happa helleipsis'");
        this.define("dispel magical protection", "harm", "none", "'removezzzzzarmour'");

        // Stun spells

        this.define("paralyze", "stun", "none", "'vorek ky taree'");
        this.define("mindseize", "stun", "none", "'diir mieelis sxil miarr mieelin'");
        this.define("tiger claw", "stun", "none", "'Haii!'");

        // Dest spells

        this.define("disintegrate", "dest", "none", "'sahanpurua'");
        this.define("acquisition", "dest", "none", "'mesmr pulrl metism'");
        this.define("destroy weapon", "dest", "none", "'rikki ja poikki'");
        this.define("destroy armour", "dest", "none", "'se on sarki nyt'");
        this.define("immolate", "dest", "none", "'fah relep krlnpth'");
    }

    public Object process(String input) {
        Spell spell = null;
        ListIterator iter = this.spells.listIterator();
        boolean found = false;
        while (iter.hasNext()) {
            spell = (Spell) iter.next();

            Matcher m = spell.pattern.matcher(input);
            if (m.find()) {
                found = true;
                break;
            }
        }
        if (found) {
            // get the caster of the spell
            return spell;
        }
        return null;
    }

    protected Object[] translateAttrs(String type, String damageType) {
        if (!damageType.equals("none"))
            return (this.damageTypeColors.containsKey(damageType)
                    ? this.damageTypeColors.get(damageType)
                    : new Object[] { TextAttribute.FOREGROUND, Color.LIGHT_GRAY });

        return (this.colors.containsKey(type)
                ? this.colors.get(type)
                : new Object[] { TextAttribute.FOREGROUND, Color.LIGHT_GRAY });
    }

    protected void define(String name, String type, String damageType, String regexp) {
        spells.add(new Spell(name, Pattern.compile(regexp), translateAttrs(type, damageType)));
    }

}
