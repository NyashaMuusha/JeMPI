import Notification, { NotificationState } from './Notification'
import {
  GoldenRecord as GR,
  PatientRecord as PR,
  AnyRecord
} from '../types/PatientRecord'

export interface NotificationRequest {
  notificationId: string
  state: NotificationState
}

export interface LinkRequest {
  goldenID: string
  patientID: string
  newGoldenID?: string
}

export interface NotificationResponse {
  records: Notification[]
  skippedRecords: number
  count: number
}

export interface GoldenRecordResponse {
  expandedGoldenRecords: ExpandedGoldenRecord[]
}

export type DemographicsData = Omit<GR, 'sourceId' | 'uid'>

export interface GoldenRecord extends Pick<GR, 'sourceId' | 'uid'> {
  demographicData: DemographicsData
  uniqueGoldenRecordData: Omit<AnyRecord, 'sourceId' | 'PatienRecord'>
}

export interface ExpandedGoldenRecord {
  goldenRecord: GoldenRecord
  interactionsWithScore: Array<InteractionWithScore>
}

export interface InteractionWithScore {
  interaction: Interaction
  score: number
}

export interface Interaction extends Pick<PR, 'sourceId' | 'uid'> {
  demographicData: DemographicsData
  uniqueInteractionData: Omit<AnyRecord, 'sourceId' | 'PatienRecord'>
}

export interface CustomGoldenRecord
  extends Omit<GoldenRecord, 'uniqueGoldenRecordData' | 'uid'> {
  goldenId: string
  customUniqueGoldenRecordData: Omit<AnyRecord, 'sourceId' | 'PatienRecord'>
}
