package exporter.util

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*

fun addValuesToSheet(spreadsheets: Sheets.Spreadsheets, spreadsheetId: String?, range: String?, data: List<List<*>>) {
  spreadsheets
      .values()
      .append(
          spreadsheetId,
          range,
          ValueRange().setMajorDimension("ROWS").setValues(data))
      .setValueInputOption("USER_ENTERED")
      .execute()
}

fun createSheet(sheetName: String): Request {
  return Request().setAddSheet(AddSheetRequest().setProperties(SheetProperties().setTitle(sheetName)))
}

fun clearSheet(sheetId: Int): Request {
  return Request().setUpdateCells(
      UpdateCellsRequest()
          .setRange(GridRange().setSheetId(sheetId))
          .setFields("*"))
}

fun conditionFormatting(sheetId: Int, data: List<List<Any>>): List<Request> {
  val requests = data
      .mapIndexed { row, value ->
        value.mapIndexed { column, cell ->
          if (column == 0) {
            null
          } else {
            Request().setAddConditionalFormatRule(AddConditionalFormatRuleRequest()
                .setRule(ConditionalFormatRule()
                    .setRanges(listOf(GridRange()
                        .setSheetId(sheetId)
                        .setStartRowIndex(1 + row)
                        .setStartColumnIndex(column)
                        .setEndColumnIndex(1 + column)
                        .setEndRowIndex(2 + row)))
                    .setBooleanRule(BooleanRule()
                        .setCondition(
                            if (cell.toString().isNotEmpty()) {
                              BooleanCondition()
                                  .setType("TEXT_NOT_CONTAINS")
                                  .setValues(listOf(ConditionValue().setUserEnteredValue(cell.toString())))
                            } else {
                              BooleanCondition().setType("NOT_BLANK")
                            })
                        .setFormat(CellFormat().setBackgroundColor(Color()
                            .setRed(.42f)
                            .setGreen(.35f)
                            .setBlue(.80f))))))
          }
        }
      }
      .flatten()
      .filterNotNull()

  val result = requests.toMutableList()

  result.add(Request().setAddConditionalFormatRule(
      AddConditionalFormatRuleRequest()
          .setRule(ConditionalFormatRule()
              .setRanges(listOf(GridRange()
                  .setSheetId(sheetId)
                  .setStartRowIndex(1)
                  .setStartColumnIndex(0)
                  .setEndColumnIndex(8)
                  .setEndRowIndex(data.size + 1)))
              .setBooleanRule(BooleanRule()
                  .setCondition(BooleanCondition().setType("BLANK"))
                  .setFormat(CellFormat().setBackgroundColor(Color()
                      .setRed(.69f)
                      .setGreen(.12f)
                      .setBlue(.18f))))
          )))

  return result
}

fun removeAllConditionalFormatting(sheetId: Int, number: Int): List<Request> {
  return (1..number).map {
    Request().setDeleteConditionalFormatRule(DeleteConditionalFormatRuleRequest().setSheetId(sheetId).setIndex(0))
  }
}

fun deleteAlternateColors(alternateColorRangeId: Int): Request {
  return Request().setDeleteBanding(DeleteBandingRequest().setBandedRangeId(alternateColorRangeId))
}

fun setAlternateColors(sheetId: Int, size: Int): Request {
  return Request().setAddBanding(AddBandingRequest().setBandedRange(BandedRange()
      .setRange(GridRange().setSheetId(sheetId).setEndColumnIndex(12).setEndRowIndex(size + 1))
      .setRowProperties(BandingProperties()
          .setHeaderColor(Color().setRed(.5f).setGreen(.5f).setBlue(.5f))
          .setFirstBandColor(Color().setRed(.9f).setGreen(.9f).setBlue(.9f))
          .setSecondBandColor(Color().setRed(.8f).setGreen(.8f).setBlue(.8f)))))
}

fun setBoldHeader(sheetId: Int): Request {
  return Request().setRepeatCell(RepeatCellRequest()
      .setRange(GridRange().setEndRowIndex(1).setEndColumnIndex(12).setSheetId(sheetId))
      .setCell(CellData().setUserEnteredFormat(CellFormat().setTextFormat(TextFormat().setBold(true))))
      .setFields("userEnteredFormat(textFormat)"))
}

fun createHeader(spreadsheets: Sheets.Spreadsheets, spreadsheetId: String, range: String, header: List<*>) {
  spreadsheets
      .values()
      .update(
          spreadsheetId,
          range,
          ValueRange()
              .setMajorDimension("ROWS")
              .setValues(listOf(header)))
      .setValueInputOption("USER_ENTERED")
      .execute()
}

fun resizeCells(sheetId: Int, startRow: Int): Request {
  return Request().setUpdateDimensionProperties(
      UpdateDimensionPropertiesRequest()
          .setRange(DimensionRange().setSheetId(sheetId).setDimension("ROWS").setStartIndex(startRow))
          .setProperties(DimensionProperties().setPixelSize(150))
          .setFields("pixelSize"))
}

fun getAllSheetTitles(spreadsheets: Sheets.Spreadsheets, spreadsheetId: String): Map<String, Int> {
  return ((spreadsheets.get(spreadsheetId)
      .setFields("sheets.properties")
      .execute()
      .values
      .single() as ArrayList<Sheet>)
      .map { it["properties"] } as ArrayList<SheetProperties>)
      .map { it["title"].toString() to Integer.valueOf(it["sheetId"].toString()) }
      .toMap()
}

fun getNumberOfConditionalFormatting(spreadsheets: Sheets.Spreadsheets, spreadsheetId: String, sheetName: String): Int {
  try {
    return ((spreadsheets.get(spreadsheetId)
        .setRanges(listOf(sheetName))
        .setFields("sheets.conditionalFormats")
        .execute()
        .values
        .single() as ArrayList<Sheet>)
        .single()["conditionalFormats"] as ArrayList<*>)
        .size
  } catch (e: Exception) {
    //TODO: Handle with something better than a try catch
    return 0
  }
}

fun getAlternateColorRangeId(spreadsheets: Sheets.Spreadsheets, spreadsheetId: String, sheetName: String): Int {
  try {
    return ((spreadsheets.get(spreadsheetId)
        .setRanges(listOf(sheetName))
        .setFields("sheets.bandedRanges")
        .execute()
        .values
        .single() as ArrayList<Sheet>)
        .single()["bandedRanges"] as ArrayList<BandedRange>)
        .single()["bandedRangeId"] as Int
  } catch (e: Exception)  {
    //TODO: Handle with something better than a try catch
    return 0
  }
}