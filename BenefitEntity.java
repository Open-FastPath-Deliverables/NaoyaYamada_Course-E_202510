package com.example.loyaltyprogram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * BenefitEntity
 * ロイヤルティ特典情報を管理するデータモデル。
 */
@Entity
@Table(name = "benefits")
public class BenefitEntity {

    // --- フィールド定義 ---

    /**
     * 特典識別子（主キー）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 特典に紐づくステージID（外部キー）
     * StageEntityとの関連付け
     */
    @ManyToOne
    @JoinColumn(name = "stage_id", referencedColumnName = "id", nullable = false)
    private StageEntity stage;

    /**
     * 特典内容（例: "全商品10%割引", "送料無料"）
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * 特典開始日時
     */
    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    /**
     * 特典終了日時
     */
    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    /**
     * 特典登録日時
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- コンストラクタ ---

    public BenefitEntity() {
    }

    public BenefitEntity(StageEntity stage, String description, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.stage = stage;
        this.description = description;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StageEntity getStage() {
        return stage;
    }

    public void setStage(StageEntity stage) {
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // --- ヘルパーメソッド ---

    /**
     * 特典が現在有効かを判定します。
     *
     * @return true: 特典が有効 / false: 特典が無効
     */
    public boolean isBenefitActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validFrom) && now.isBefore(validUntil);
    }

    /**
     * 特典の詳細を出力する。
     *
     * @return 特典の詳細文字列
     */
    @Override
    public String toString() {
        return "BenefitEntity{" +
                "id=" + id +
                ", stage=" + (stage != null ? stage.getId() : "未設定") +
                ", description='" + description + '\'' +
                ", validFrom=" + validFrom +
                ", validUntil=" + validUntil +
                ", createdAt=" + createdAt +
                '}';
    }
}
