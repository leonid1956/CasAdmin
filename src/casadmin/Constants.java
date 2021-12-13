package casadmin;

import java.util.*;

public class Constants {

    public static final String temporaryDir = "Temp";
    public static final String linkWLink = "http://localhost:5559";
    public static final boolean bShowAnimatedIcon = false;
    public enum gifStyle {about,bigerror,edit,error,hello,ok,process,read,talk,wait};
    public enum tableTypes {nullType, boolType, intType, doubleType, stringType};

     /**
     * <p><strong>Szablon</strong> formatowania daty</p>
     */
    public static final String DateFormat = "yyyy/MM/dd";
     /**
     * <p>Data, interpretowana jako <strong>początkowa</strong></p>
     */
    public static final Date NullDate = new Date(1900, 01, 01);

    public static final String brakZawartosci = "brakzawartosci";

     /**
     * <p>Nazwy pól w ciągu znaków, przesyłanych klientskimi aplikacjami do serwera Cas,
     * nazywany <strong>String PDF</strong></p>
     */
    public static final String PdfItemsNames[] = {
        "Header",
        "ACT_Mode",
        "LAB_Flag",
        "Shipment_ID",
        "SEN_ID",
        "SEN_Name",
        "SEN_Postcode",
        "SEN_City",
        "SEN_Street",
        "SEN_Housenumber",
        "SEN_Telefon",
        "REC_ID",
        "REC_Name",
        "REC_Postcode",
        "REC_City",
        "REC_Street",
        "REC_Housenumber",
        "REC_Telefon",
        "THI_ID",
        "THI_Name",
        "THI_Postcode",
        "THI_City",
        "THI_Street",
        "THI_Housenumber",
        "THI_Telefon",
        "Product",
        "Invoice_To",
        "Payment_Type",
        "Num_Letter",
        "Num_upto5",
        "Num_5to10",
        "Num_10to20",
        "Num_20to31",
        "Num_nonstd",
        "Num_pallets",
        "Num_Pal_upto200",
        "Num_Pal_200to400",
        "Num_Pal_over400",
        "Total_Weight",
        "Weight_Type",
        "Num_Pal_Euro",
        "CTD",
        "Costs_Center",
        "Comment",
        "Pickup_Depot",
        "Tour_ID",
        "Pickup_Timestamp",
        "Sevice_Flags",
        "SC_0",
        "SC_1",
        "SC_2",
        "SC_3",
        "SC_4",
        "SC_5 UBEZPIECZNIE W CENIE",
        "SC_6",
        "SC_7",
        "SC_8",
        "SC_9 OPŁATA usługa premium",
        "CHA_EX",
        "CHA_PAL",
        "COD",
        "CHA_COD",
        "Goods_Value",
        "CHA_INS",
        "CHA_CTD",
        "CHA_NST",
        "CHA_SUM",
        "DISTANCE",
        "REGION_ODB",
        "COD_REFR",
        "FIRST_ITEM_NO",
        "PAL_B1",
        "PAL_B2",
        "SPERGUT_GR",
        "OPL_SPERGUT_DR",
        "TNTROD_Data",
        "TNTROD_ID",
        "TNTROD_Nazwa",
        "TNTROD_Dzial",
        "TNTROD_Kod_P",
        "TNTROD_Miasto",
        "TNTROD_Ulica",
        "TNTROD_Dom",
        "TNTROD_Pokoj",
        "Nad_Mail",
        "Nad_Phone",
        "Odb_Mail",
        "Odb_DodTekst",
        "Odb_Phone",
        "EN_Kraj",
        "EN_Wymiary",
        "EN_Terminal",
        "EN_PostCode",
        "Rec_Reference",
        "ToServicePoint",
        "FinalReceiverID",
        "FinalReceiverName",
        "FinalReceiverPostCode",
        "FinalReceiverCity",
        "FinalReceiverStreet",
        "FinalReceiverHouse",
        "FinalReceiverTel",
        "FinalReceiverMail",
        "PDI_Selected",
        "PDI_Quote",
        "VIP_Selected",
        "VIP_Quote",
        "RodDocument",
        "Trace",
        "User_Info",
        "Contact_Person",
//XBorder zmiana 
        "EK_Country",
        "EK_PackStation",
        "EK_ClientNumber",
        "NeighbourName",
        "NeighbourPostCode",
        "NeighbourCity",
        "NeighbourStreet",
        "NeighbourHouse",
        "NeighbourTel",
        "EK_PostFiliale",
        "COD Account",
        "Number_25I",
        "Item Length",
        "Item Width",
        "Item Height",
        "Utilization",
        "Pnp By Mail",
        "Content",
        "ExpireDate",
        "Book_courier",
        "Related_waybill",
        "SenderCountry",
        "RecType",
        "DeliveryDays",
            
            
"Ad_hoc_pickup",
"Courier_with_label",
"DQO_SEN_out_miejscowosc",
"DQO_SEN_out_ulica",
"DQO_SEN_out_nr_domu",
"DQO_SEN_out_nr_miesz",
"DQO_SEN_out_kod",
"DQO_SEN_statusCode",
"DQO_SEN_out_status_adr_licz",
"DQO_SEN_out_wsp_x",
"DQO_SEN_out_wsp_y",
"DQO_SEN_strefa",
"DQO_REC_out_miejscowosc",
"DQO_REC_out_ulica",
"DQO_REC_out_nr_domu",
"DQO_REC_out_nr_miesz",
"DQO_REC_out_kod",
"DQO_REC_statusCode",
"DQO_REC_out_status_adr_licz",
"DQO_REC_out_wsp_x",
"DQO_REC_out_wsp_y",
"DQO_REC_out_mieszkania",
"DQO_REC_out_osoby_prawne",
"DQO_REC_budynek_dhl",
"DQO_REC_obszar",
"DQO_REC_strefa",
"Typ paczki/palety",
"Typ rabatu",
"Nazwa rabatu",
"Wartość rabatu",
"VAT",
"Opłata nadanie kurierem",
"Opłata doręczenie kurierem",
"Opłata wydruk etykiety kurier/punkt",
"Wybrana usługa Premium",
"NUM_PALITEM_UPTO40",
"NUM_PAL_40TO50",
"NUM_PAL_50TO100"

    };
    /**
    * <p>Mapa wersji <strong>stringu PDF</strong> do ilości <strong>tokenów</strong> w ciągu</p>
    */
    public static final Map<String, Integer> PdfStringLength =
            new HashMap<String, Integer>() {

                {
                    put("DHL0201", 69);
                    put("DHL0202", 70);
                    put("DHL0203", 71);
                    put("DHL0204", 75);
                    put("DHL0205", 84);
                    put("DHL0206", 89);
                    put("DHL0207", 93);
                    put("DHL0208", 94);
                    put("DHL0209", 103);
                    put("DHL0210", 107);
                    put("DHL0211", 108);
                    put("DHL0212", 109);
                    put("DHL0213", 110);
                    put("DHL0214", 111);
//XBORDER
                    put("DHL0215", 114);
                    put("DHL0216", 120);
                    put("DHL0217", 121);
                    put("DHL0218", 122);
                    put("DHL0219", 127);
                    put("DHL0220", 129);
                    put("DHL0221", 133);
                    put("DHL0222", 135);
                    put("DHL0223", 137);
                    put("DHL0224", 161);
                    put("DHL0225", 170);
                    put("DHL0226", 173);
                    put("DHL0227", 173);
                    // rezerwa
                }
            };
}
