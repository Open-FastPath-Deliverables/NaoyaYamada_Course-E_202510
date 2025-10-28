package com.example.loyaltyprogram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * StageEntity
 * ユーザーのロイヤルティプログラム内のステージ情報を管理するデータモデル。
 */
@Entity
@Table(name = "stages")
public class StageEntity {

    // --- フィールド定義 ---

    /**
     * ステージ識別子（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザーID（外部キー）
     * UserEntityとの関連付け
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    /**
     * 現在のロイヤルティステージ（例: ゴールド, シルバー, ブロンズ）
     */
    @Column(name = "stage_name", nullable = false)
    private String stageName;

    /**
     * ステージ昇格条件（必要条件などポイント閾値に基づく）
     */
    @Column(name = "promotion_criteria", nullable = false)
    private int promotionCriteria;

    /**
     * 初めてステージが適用された日時
     */
    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    /**
     * ステージ情報の最終更新日時
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- コンストラクタ ---

    public StageEntity() {
    }

    public StageEntity(UserEntity user, String stageName, int promotionCriteria) {
        this.user = user;
        this.stageName = stageName;
        this.promotionCriteria = promotionCriteria;
        this.appliedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public int getPromotionCriteria() {
        return promotionCriteria;
    }

    public void setPromotionCriteria(int promotionCriteria) {
        this.promotionCriteria = promotionCriteria;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- ヘルパーメソッド ---

    /**
     * ステージを更新し、更新日時を自動設定します。
     *
     * @param newStageName 新しいステージ名
     */
    public void updateStage(String newStageName) {
        this.stageName = newStageName;
        this.updatedAt = LocalDateTime.now(); // ステージ更新時のタイムスタンプ更新
    }
}